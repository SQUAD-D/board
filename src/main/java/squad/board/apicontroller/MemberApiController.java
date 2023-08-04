package squad.board.apicontroller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import squad.board.domain.member.Member;
import squad.board.dto.member.CreateMemberDto;
import squad.board.dto.member.LoginMemberDto;
import squad.board.dto.member.ValidateLoginIdDto;
import squad.board.service.MemberService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 회원가입 요청
    @PostMapping("/members/new")
    public ResponseEntity<Long> createMember(@RequestBody CreateMemberDto createMemberDto) {
        return ResponseEntity.ok(memberService.join(createMemberDto));
    }

    // 중복 아이디 검증
    @PostMapping("/members/validation")
    public ResponseEntity<String> validationLoginId(@RequestBody ValidateLoginIdDto loginIdDto){
        return ResponseEntity.ok(memberService.validationLoginId(loginIdDto.getLoginId()));
    }

    @PostMapping("/members/login")
    public ResponseEntity<String> loginMember(
            @RequestBody LoginMemberDto loginMemberDto,
            HttpServletRequest request){
        Member member = memberService.login(loginMemberDto);
        return ResponseEntity.ok(memberService.sessionValidation(member,request));
    }
}
