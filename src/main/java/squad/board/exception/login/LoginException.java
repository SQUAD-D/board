package squad.board.exception.login;

import lombok.Getter;
import squad.board.exception.CommonException;

@Getter
public class LoginException extends CommonException {
    public LoginException(LoginStatus status) {
        super(status);
    }
}
