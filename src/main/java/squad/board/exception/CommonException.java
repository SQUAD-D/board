package squad.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import squad.board.commonresponse.CommonStatus;

@Getter
@AllArgsConstructor
public class CommonException extends RuntimeException {
    private CommonStatus commonStatus;
}
