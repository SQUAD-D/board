package squad.board.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class LoginResponse {
    private Long memberId;
    private String nickName;
}
