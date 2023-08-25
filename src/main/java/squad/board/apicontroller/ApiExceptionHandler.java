package squad.board.apicontroller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import squad.board.commonresponse.CommonArgumentResponse;
import squad.board.commonresponse.CommonExceptionResponse;
import squad.board.commonresponse.CommonStatus;
import squad.board.exception.CommonException;
import squad.board.exception.board.BoardException;
import squad.board.exception.login.LoginException;
import squad.board.exception.session.SessionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    // 로그인관련 예외 처리
    @ExceptionHandler(LoginException.class)
    public CommonExceptionResponse<CommonStatus> loginExceptionHandler(
            HttpServletResponse response,
            LoginException loginException) {
        setStatusCode(response, loginException);
        return new CommonExceptionResponse<>(loginException.getCommonStatus());
    }

    // 세션 검증 실패 시 로그인화면으로 리다이렉션
    @ExceptionHandler(SessionException.class)
    public ModelAndView sessionExceptionHandler(
            HttpServletResponse response,
            SessionException sessionException) {
        setStatusCode(response, sessionException);
        log.info("Session Validation Failed");
        return new ModelAndView("redirect:/");
    }

    // 게시글 관련 예외 처리
    @ExceptionHandler(BoardException.class)
    public CommonExceptionResponse<CommonStatus> boardExceptionHandler(
            HttpServletResponse response,
            BoardException boardException
    ) {
        setStatusCode(response, boardException);
        return new CommonExceptionResponse<>(boardException.getCommonStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonArgumentResponse inputExceptionHandler(
            MethodArgumentNotValidException inputException
    ) {
        List<FieldError> fieldErrors = inputException.getBindingResult().getFieldErrors();
        return new CommonArgumentResponse(fieldErrors.get(0).getDefaultMessage());
    }

    private void setStatusCode(HttpServletResponse response, CommonException commonException) {
        int statusCode = commonException.getCommonStatus().getHttpStatusCode().value();
        response.setStatus(statusCode);
    }
}
