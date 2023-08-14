package squad.board.apicontroller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.commonresponse.CommonResponse;
import squad.board.domain.member.Member;
import squad.board.dto.member.CreateMemberDto;
import squad.board.dto.member.LoginRequestDto;
import squad.board.dto.member.ValidateLoginIdDto;
import squad.board.exception.login.LoginStatus;
import squad.board.service.MemberService;

import static squad.board.exception.login.LoginStatus.SUCCESS;
import static squad.board.exception.login.LoginStatus.VALID_LOGIN_ID;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 회원가입 요청
    @PostMapping("/new")
    public ResponseEntity<Long> createMember(@RequestBody CreateMemberDto createMemberDto) {
        return ResponseEntity.ok(memberService.join(createMemberDto));
    }

    // 중복 아이디 검증
    @PostMapping("/validation")
    public CommonResponse<Void, LoginStatus> validationLoginId(@RequestBody ValidateLoginIdDto loginIdDto) {
        memberService.validationLoginId(loginIdDto.getLoginId());
        return new CommonResponse<>(VALID_LOGIN_ID, null);
    }

    // 로그인
    @PostMapping("/login")
    public CommonResponse<CommonIdResponse, LoginStatus> loginMember(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest request) {
        Member member = memberService.login(loginRequestDto);
        return new CommonResponse<>(SUCCESS, memberService.provideSession(member, request));
    }
}
