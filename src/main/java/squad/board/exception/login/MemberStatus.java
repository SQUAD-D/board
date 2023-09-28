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
public enum MemberStatus implements CommonStatus {
    INVALID_LOGIN_INFO(HttpStatus.valueOf(400), 101, "로그인 정보를 찾을 수 없습니다."),
    DUPLICATED_LOGIN_ID(HttpStatus.valueOf(400), 103, "중복된 아이디입니다."),
    DUPLICATED_NICK_NAME(HttpStatus.valueOf(400), 104, "중복된 닉네임입니다."),
    DUPLICATION_CHK_REQUIRED(HttpStatus.valueOf(400), 105, "중복체크를 해주세요.");

    private final HttpStatus httpStatusCode;
    private final int code;
    private final String message;

    MemberStatus(HttpStatus httpStatusCode, int code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
