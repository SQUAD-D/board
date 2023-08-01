package squad.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import squad.board.domain.member.Member;
import squad.board.dto.member.CreateMemberDto;
import squad.board.dto.member.LoginMemberDto;
import squad.board.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 화면
    @GetMapping("/members/new")
    public String createMemberView(Model model){
        model.addAttribute("createMemberDto",new CreateMemberDto());
        return "/member/createMember";
    }

    // 로그인 화면
    @GetMapping("/members/login")
    public String loginMemberView(Model model){
        model.addAttribute("loginMemberDto",new LoginMemberDto());
        return "/member/loginMember";
    }

    @PostMapping("/members/login")
    public String loginMember(
            @ModelAttribute LoginMemberDto loginMemberDto,
            HttpServletRequest request,
            BindingResult bindingResult){
        Member member = memberService.login(loginMemberDto);

        // 로그인 정보 검증
        if(member == null){
            bindingResult.reject("로그인 정보가 잘못됐습니다.");
        }

        // 기존 세션은 파기
        request.getSession().invalidate();
        // 세션이 없다면 새로운 세션 생성
        HttpSession session = request.getSession(true);
        // 세션에 member의 PK 값을 Value로 세팅
        session.setAttribute("memberId",member.getMemberId());
        // 유효 시간은 30분
        session.setMaxInactiveInterval(1800);

        return "redirect:/";
    }

    @PostMapping("/members/new")
    public String createMember(@ModelAttribute CreateMemberDto createMemberDto) {
        memberService.join(createMemberDto);
        return "redirect:/members/login";
    }

}
