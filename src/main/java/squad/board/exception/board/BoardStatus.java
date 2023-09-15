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
    INVALID_DELETE_UPDATE(HttpStatus.valueOf(400), 302, "작성자 이외엔 게시글을 수정/삭제할 수 없습니다."),
    INVALID_PAGE_NUMBER(HttpStatus.valueOf(400), 303, "존재하지않는 페이지입니다.");
    private final HttpStatus httpStatusCode;
    private final int code;
    private final String message;

    BoardStatus(HttpStatus httpStatusCode, int code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
