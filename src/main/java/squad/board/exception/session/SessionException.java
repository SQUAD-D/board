package squad.board.exception.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SessionException extends RuntimeException {
    private SessionStatus sessionStatus;
}
