package squad.board.dto.member;

import lombok.*;
import squad.board.domain.member.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMemberDto {
    private String loginId;
    private String loginPw;
    private String name;
    private String nickName;

    public Member toEntity(){
        return Member.builder()
                .loginId(loginId)
                .loginPw(loginPw)
                .name(name)
                .nickName(nickName)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
