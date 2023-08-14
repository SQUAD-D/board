package squad.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import squad.board.service.MemberService;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final MemberService memberService;

    // 게시판 화면
    @GetMapping("/boards")
    public String boardListView(HttpServletRequest request) {
        memberService.validateSession(request);
        return "/board/boardList";
    }

    // 게시글 작성 화면
    @GetMapping("/boards/new")
    public String createBoard(HttpServletRequest request) {
        memberService.validateSession(request);
        return "/board/createBoard";
    }
}
