package squad.board.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import squad.board.domain.member.Member;

@Getter
@Setter
public class MemberResponse {
    private String loginId;

    private String loginPw;

    private String name;

    private String nickName;
    
    public MemberResponse(Member member) {
        this.loginId = member.getLoginId();
        this.loginPw = member.getLoginPw();
        this.name = member.getName();
        this.nickName = member.getNickName();
    }
}
