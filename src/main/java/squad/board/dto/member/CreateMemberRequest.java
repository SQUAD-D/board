package squad.board.dto.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import squad.board.domain.member.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CreateMemberRequest {
    @NotEmpty(message = "아이디를 입력해주세요.")
    private String loginId;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 6, message = "6자 이상으로 입력해주세요.")
    private String loginPw;
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    @NotEmpty(message = "닉네임을 입력해주세요.")
    private String nickName;

    public Member toEntity() {
        return Member.builder()
                .loginId(loginId)
                .loginPw(loginPw)
                .name(name)
                .nickName(nickName)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
