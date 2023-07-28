package squad.board.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    // 회원가입 화면
    @GetMapping("/members/new")
    public String createMember(){
        return "/member/createMember";
    }

    // 로그인 화면
    @GetMapping("/member/login")
    public String loginMember(){
        return "/member/loginMember";
    }

}
