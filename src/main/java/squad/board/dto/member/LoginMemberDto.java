package squad.board.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginMemberDto {
    private String loginId;
    private String loginPw;
}