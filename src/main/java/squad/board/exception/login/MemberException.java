package squad.board.exception.login;

import lombok.Getter;
import squad.board.exception.CommonException;

@Getter
public class MemberException extends CommonException {
    public MemberException(MemberStatus status) {
        super(status);
    }
}
