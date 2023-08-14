package squad.board.exception.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardException extends RuntimeException {
    private BoardStatus boardStatus;
}
