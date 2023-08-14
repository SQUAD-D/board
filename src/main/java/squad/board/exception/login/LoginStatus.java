package squad.board.exception.login;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import squad.board.commonresponse.CommonStatus;

/**
 * LoginStatus [ 로그인 응답 코드 ]
 *
 * @Code:100~199
 */
@Getter
public enum LoginStatus implements CommonStatus {
    SUCCESS(HttpStatus.valueOf(200), 100, "로그인 성공"),
    INVALID_LOGIN_INFO(HttpStatus.valueOf(400), 101, "로그인 정보를 찾을 수 없습니다."),
    VALID_LOGIN_ID(HttpStatus.valueOf(200), 102, "사용가능한 아이디입니다."),
    DUPLICATED_LOGIN_ID(HttpStatus.valueOf(400), 103, "중복된 아이디입니다.");

    private final HttpStatus httpStatusCode;
    private final int code;
    private final String message;

    LoginStatus(HttpStatus httpStatusCode, int code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
