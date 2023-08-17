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
    SUCCESS(HttpStatus.valueOf(200), 300, "게시글 작성 성공"),
    DELETE_SUCCESS(HttpStatus.valueOf(200), 301, "삭제 성공"),
    INVALID_DELETE_UPDATE(HttpStatus.valueOf(400), 302, "작성자 이외엔 수정/삭제할 수 없습니다."),
    UPDATE_SUCCESS(HttpStatus.valueOf(200), 303, "수정 성공");
    private final HttpStatus httpStatusCode;
    private final int code;
    private final String message;

    BoardStatus(HttpStatus httpStatusCode, int code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
