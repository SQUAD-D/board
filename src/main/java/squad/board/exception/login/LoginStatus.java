package squad.board.exception.login;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public enum LoginStatus {
    SUCCESS(HttpStatusCode.valueOf(200), 100, "로그인 성공"),
    INVALID_LOGIN_INFO(HttpStatusCode.valueOf(404), 101, "로그인 정보를 찾을 수 없습니다."),
    VALID_LOGIN_ID(HttpStatusCode.valueOf(200), 102, "사용가능한 아이디입니다."),
    DUPLICATED_LOGIN_ID(HttpStatusCode.valueOf(409), 103, "중복된 아이디입니다.");

    private final HttpStatusCode httpStatusCode;
    private final int code;
    private final String message;

    LoginStatus(HttpStatusCode httpStatusCode, int code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
