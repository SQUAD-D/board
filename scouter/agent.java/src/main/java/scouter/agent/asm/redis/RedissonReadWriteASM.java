/*
 *  Copyright 2015 the original author or authors.
 *  @https://github.com/scouter-project/scouter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package scouter.agent.asm.redis;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;
import scouter.agent.ClassDesc;
import scouter.agent.Configure;
import scouter.agent.asm.IASM;
import scouter.agent.asm.util.AsmUtil;
import scouter.agent.asm.util.HookingSet;
import scouter.agent.trace.TraceMain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2018. 3. 20.
 */
public class RedissonReadWriteASM implements IASM, Opcodes {
    private Configure conf = Configure.getInstance();

    private static List<String> hookingPattern = new ArrayList<String>();
    static {
        hookingPattern.add("org.redisson.spring.data.connection.RedissonConnection.write([BLorg/redisson/client/codec/Codec;Lorg/redisson/client/protocol/RedisCommand;[Ljava/lang/Object;)Ljava/lang/Object;");
        hookingPattern.add("org.redisson.spring.data.connection.RedissonConnection.read([BLorg/redisson/client/codec/Codec;Lorg/redisson/client/protocol/RedisCommand;[Ljava/lang/Object;)Ljava/lang/Object;");
    }
    private List<HookingSet> targetList;

    public RedissonReadWriteASM() {
        targetList = HookingSet.getHookingMethodSet(HookingSet.buildPatterns("", hookingPattern));
    }

    public ClassVisitor transform(ClassVisitor cv, String className, ClassDesc classDesc) {
        if (conf._hook_redis_enabled == false)
            return cv;

        for (int i = 0; i < targetList.size(); i++) {
            HookingSet mset = targetList.get(i);
            if (mset.classMatch.include(className)) {
                return new RedissonReadWriteCV(cv, mset, className);
            }
        }

        return cv;
    }
}

class RedissonReadWriteCV extends ClassVisitor implements Opcodes {
    String className;
    HookingSet mset;

    public RedissonReadWriteCV(ClassVisitor cv, HookingSet mset, String className) {
        super(ASM9, cv);
        this.mset = mset;
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (mv == null || mset.isA(name, desc) == false) {
            return mv;
        }
        if (AsmUtil.isSpecial(name)) {
            return mv;
        }

        return new RedissonReadWriteMV(access, name, desc, mv);
    }
}

class RedissonReadWriteMV extends LocalVariablesSorter implements Opcodes {
    private static final String TRACEMAIN = TraceMain.class.getName().replace('.', '/');
    private static final String START_METHOD = "startRedissonCommand";
    private static final String START_SIGNATURE = "(Ljava/lang/Object;[BLjava/lang/Object;)Ljava/lang/Object;";
    private static final String END_METHOD = "endRedissonCommand";
    private static final String END_SIGNATURE = "(Ljava/lang/Object;Ljava/lang/Throwable;)V";
    private String name;
    private String desc;
    private Type returnType;
    private int statIdx;
    private Label startFinally = new Label();

    public RedissonReadWriteMV(int access, String name, String desc, MethodVisitor mv) {
        super(ASM9, access, desc, mv);
        this.name = name;
        this.desc = desc;
        this.returnType = Type.getReturnType(desc);
    }

    @Override
    public void visitCode() {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, TRACEMAIN, START_METHOD, START_SIGNATURE, false);
        statIdx = newLocal(Type.getType(Object.class));
        mv.visitVarInsn(Opcodes.ASTORE, statIdx);
        mv.visitLabel(startFinally);
        mv.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= IRETURN && opcode <= RETURN)) {
            mv.visitVarInsn(Opcodes.ALOAD, statIdx);
            mv.visitInsn(Opcodes.ACONST_NULL);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, TRACEMAIN, END_METHOD, END_SIGNATURE, false);
        }
        mv.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        Label endFinally = new Label();
        mv.visitTryCatchBlock(startFinally, endFinally, endFinally, null);
        mv.visitLabel(endFinally);
        mv.visitInsn(DUP);
        int errIdx = newLocal(Type.getType(Throwable.class));
        mv.visitVarInsn(Opcodes.ASTORE, errIdx);

        mv.visitVarInsn(Opcodes.ALOAD, statIdx);
        mv.visitVarInsn(Opcodes.ALOAD, errIdx);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, TRACEMAIN, END_METHOD, END_SIGNATURE, false);
        mv.visitInsn(ATHROW);
        mv.visitMaxs(maxStack + 8, maxLocals + 2);
    }
}
