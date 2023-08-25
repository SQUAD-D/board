package squad.board.commonresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommonExceptionResponse<S extends CommonStatus> { //BaseResponse 객체를 사용할때 성공, 실패 경우
    private final String message;
    private final int code;

    public CommonExceptionResponse(S status) {
        this.message = status.getMessage();
        this.code = status.getCode();
    }
}