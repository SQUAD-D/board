package squad.board.exception.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginException extends RuntimeException{
    private LoginStatus loginStatus;
}
