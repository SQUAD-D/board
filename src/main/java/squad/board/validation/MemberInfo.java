package squad.board.validation;

import squad.board.exception.login.MemberException;
import squad.board.repository.MemberMapper;

public interface MemberInfo {
    void duplicationChk(MemberMapper memberMapper) throws MemberException;
}
