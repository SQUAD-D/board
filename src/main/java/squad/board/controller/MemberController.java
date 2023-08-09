package squad.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import squad.board.dto.member.CreateMemberDto;
import squad.board.dto.member.LoginRequestDto;
import squad.board.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 화면
    @GetMapping("/members/new")
    public String createMemberView(Model model) {
        model.addAttribute("createMemberDto", new CreateMemberDto());
        return "/member/createMember";
    }

    // 로그인 화면
    @GetMapping("/members/login")
    public String loginMemberView(Model model) {
        model.addAttribute("loginMemberDto", new LoginRequestDto());
        return "/member/loginMember";
    }
}
