package squad.board.exception.comment;

import org.springframework.http.HttpStatus;
import squad.board.exception.CommonException;

public class CommentException extends CommonException {
    public CommentException(CommentStatus status) {
        super(status);
    }
}
