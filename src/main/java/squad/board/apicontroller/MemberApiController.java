package squad.board.apicontroller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import squad.board.argumentresolver.SessionAttribute;
import org.springframework.web.bind.annotation.*;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.member.Member;
import squad.board.dto.member.*;
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
    @PostMapping("/id-validation")
    public void idValidation(
            @RequestParam String loginId) {
        memberService.validationMemberInfo(loginId);
    }

    // 중복 닉네임 검증
    @PostMapping("/nick-validation")
    public void nickValidation(
            @RequestParam String nickName
    ) {
        memberService.validationMemberInfo(nickName);
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

    // 회원 단건 조회
    @GetMapping
    public MemberResponse findMember(
            @squad.board.argumentresolver.SessionAttribute Long memberId
    ) {
        return memberService.findMember(memberId);
    }

    // 회원 정보 수정
    @PatchMapping
    public void updateMember(
            @SessionAttribute Long memberId,
            @Valid @RequestBody MemberUpdateRequest memberUpdateRequest
    ) {
        memberService.updateMember(memberId, memberUpdateRequest);
    }
}
