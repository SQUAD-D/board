package squad.board.exception.board;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import squad.board.commonresponse.CommonStatus;

/**
 * BoardStatus [ 게시판 응답 코드 ]
 *
 * @Code:300~399
 */
@Getter
public enum BoardStatus implements CommonStatus {
    SUCCESS(HttpStatus.valueOf(200), 300, "게시글 작성 성공");

    private final HttpStatus httpStatusCode;
    private final int code;
    private final String message;

    BoardStatus(HttpStatus httpStatusCode, int code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
