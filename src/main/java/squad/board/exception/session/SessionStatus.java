package squad.board.exception.session;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import squad.board.commonresponse.CommonStatus;

/**
 * SessionStatus [ 세션 응답 코드 ]
 *
 * @Code:200~299
 */
@Getter
public enum SessionStatus implements CommonStatus {
    INVALID_SESSION_ID(HttpStatus.valueOf(302), 200, "세션 만료");

    private final HttpStatus httpStatusCode;
    private final int code;
    private final String message;

    SessionStatus(HttpStatus httpStatusCode, int code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
