package squad.board.validation;

import lombok.AllArgsConstructor;
import squad.board.exception.login.MemberException;
import squad.board.exception.login.MemberStatus;
import squad.board.repository.MemberMapper;

@AllArgsConstructor
public class MemberNickName implements MemberInfo {

    private final String nickName;

    @Override
    public void duplicationChk(MemberMapper memberMapper) throws MemberException {
        memberMapper.findByNickName(nickName).ifPresent(member -> {
            throw new MemberException(MemberStatus.DUPLICATED_NICK_NAME);
        });
    }
}
