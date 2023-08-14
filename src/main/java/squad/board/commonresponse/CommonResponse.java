package squad.board.commonresponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonPropertyOrder({"code", "message", "result"})
public class CommonResponse<T, S extends CommonStatus> { //BaseResponse 객체를 사용할때 성공, 실패 경우
    private final String message;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public CommonResponse(S status, T result) {
        this.message = status.getMessage();
        this.code = status.getCode();
        this.result = result;
    }
}