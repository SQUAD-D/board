package squad.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/my-page")
@Controller
public class MyPageController {

    @GetMapping("/my-boards")
    public String myBoards() {
        return "/mypage/myBoards";
    }

    @GetMapping("/my-comments")
    public String myComments() {
        return "/mypage/myComments";
    }

    @GetMapping("/info-update")
    public String infoUpdate() {
        return "/mypage/infoUpdate";
    }
}
