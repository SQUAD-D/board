package squad.board.commonresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonArgumentResponse {
    // 필드에러 code 500번
    private final int code = 500;
    private final String fieldErrorMessage;
}
