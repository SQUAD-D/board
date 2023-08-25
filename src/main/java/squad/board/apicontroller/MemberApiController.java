package squad.board.apicontroller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.member.Member;
import squad.board.dto.member.CreateMemberRequest;
import squad.board.dto.member.LoginRequest;
import squad.board.dto.member.ValidateLoginIdRequest;
import squad.board.service.MemberService;

@RestController
@RequestMapping(value = "/api/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping
    public CommonIdResponse createMember(
            @RequestBody @Valid CreateMemberRequest createMember) {
        return memberService.join(createMember);
    }

    // 중복 아이디 검증
    @PostMapping("/validation")
    public void validationLoginId(
            @RequestBody @Valid ValidateLoginIdRequest loginIdDto) {
        memberService.validationLoginId(loginIdDto.getLoginId());
    }

    // 로그인
    @PostMapping("/login")
    public CommonIdResponse loginMember(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletRequest request) {
        Member member = memberService.login(loginRequest);
        return memberService.provideSession(member, request);
    }

    // 로그아웃
    @PostMapping("/logout")
    public CommonIdResponse logoutMember(
            HttpServletRequest request
    ) {
        return memberService.logout(request);
    }
}
