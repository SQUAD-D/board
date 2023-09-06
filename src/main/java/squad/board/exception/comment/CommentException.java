package squad.board.exception.comment;

import squad.board.exception.CommonException;

public class CommentException extends CommonException {

    public CommentException(CommentStatus status) {
        super(status);
    }
}
