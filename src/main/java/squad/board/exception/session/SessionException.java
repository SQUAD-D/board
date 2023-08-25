package squad.board.exception.session;

import lombok.Getter;
import squad.board.exception.CommonException;

@Getter
public class SessionException extends CommonException {
    public SessionException(SessionStatus status) {
        super(status);
    }
}
