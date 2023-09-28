package squad.board.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    private Long memberId;

    private String loginId;

    private String loginPw;

    private String name;

    private String nickName;

    private LocalDateTime createdDate;

    @Builder
    public Member(String loginId, String loginPw, String name, String nickName, LocalDateTime createdDate) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.nickName = nickName;
        this.createdDate = createdDate;
    }
}
