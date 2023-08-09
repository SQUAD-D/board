package squad.board.exception.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import static squad.board.exception.login.LoginStatus.SUCCESS;

@AllArgsConstructor
@Getter
@JsonPropertyOrder({"httpStatusCode", "message", "code", "result"})
public class LoginResponse<T> { //BaseResponse 객체를 사용할때 성공, 실패 경우
    private final HttpStatusCode httpStatusCode;
    private final String message;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public LoginResponse(LoginStatus status, T result) {
        this.httpStatusCode = status.getHttpStatusCode();
        this.message = status.getMessage();
        this.code = status.getCode();
        this.result = result;
    }
}