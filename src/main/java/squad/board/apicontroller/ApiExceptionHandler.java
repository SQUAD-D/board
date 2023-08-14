package squad.board.apicontroller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import squad.board.commonresponse.CommonResponse;
import squad.board.exception.login.LoginException;
import squad.board.exception.login.LoginStatus;
import squad.board.exception.session.SessionException;

@ControllerAdvice
public class ApiExceptionHandler {

    // 로그인관련 예외 전역 처리
    @ExceptionHandler(LoginException.class)
    public CommonResponse<Void, LoginStatus> loginExceptionHandler(
            HttpServletResponse response,
            LoginException loginException) {
        int statusCode = loginException.getLoginStatus().getHttpStatusCode().value();
        response.setStatus(statusCode);
        return new CommonResponse<>(loginException.getLoginStatus(), null);
    }

    // 세션 검증 실패 시 로그인화면으로 리다이렉션
    @ExceptionHandler(SessionException.class)
    public ModelAndView sessionExceptionHandler(
            HttpServletResponse response,
            SessionException sessionException) {
        int statusCode = sessionException.getSessionStatus().getHttpStatusCode().value();
        response.setStatus(statusCode);
        return new ModelAndView("redirect:/");
    }
}
