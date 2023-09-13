package squad.board.exception.comment;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import squad.board.commonresponse.CommonStatus;

@Getter
public enum CommentStatus implements CommonStatus {
    INVALID_DELETE_UPDATE(HttpStatus.valueOf(400), 400, "작성자 이외엔 댓글을 수정/삭제할 수 없습니다."),
    INVALID_PAGE_NUMBER(HttpStatus.valueOf(400), 401, "존재하지 않는 페이지입니다.");
    private final HttpStatus httpStatusCode;
    private final int code;
    private final String message;

    CommentStatus(HttpStatus httpStatusCode, int code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
