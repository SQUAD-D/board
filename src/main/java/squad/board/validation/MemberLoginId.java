package squad.board.validation;

import lombok.AllArgsConstructor;
import squad.board.exception.login.MemberException;
import squad.board.exception.login.MemberStatus;
import squad.board.repository.MemberMapper;

@AllArgsConstructor
public class MemberLoginId implements MemberInfo {
    private final String loginId;

    @Override
    public void duplicationChk(MemberMapper memberMapper) throws MemberException {
        memberMapper.findByLoginId(loginId).ifPresent(member -> {
            throw new MemberException(MemberStatus.DUPLICATED_LOGIN_ID);
        });
    }
}
