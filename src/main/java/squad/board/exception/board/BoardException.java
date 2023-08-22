package squad.board.exception.board;

import lombok.Getter;
import squad.board.exception.CommonException;

@Getter
public class BoardException extends CommonException {
    public BoardException(BoardStatus status) {
        super(status);
    }
}
