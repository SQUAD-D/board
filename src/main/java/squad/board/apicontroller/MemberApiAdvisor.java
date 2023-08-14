package squad.board.apicontroller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import squad.board.exception.login.LoginException;
import squad.board.exception.login.LoginResponse;

@RestControllerAdvice
public class MemberApiAdvisor {

    @ExceptionHandler(LoginException.class)
    public LoginResponse<Void> loginExceptionHandler(LoginException loginException) {
        return new LoginResponse<>(loginException.getLoginStatus(), null);
    }
}
