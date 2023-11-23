package squad.board.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ValidateLoginIdRequest {
    @NotEmpty(message = "아이디를 입력해주세요.")
    private String loginId;
}
