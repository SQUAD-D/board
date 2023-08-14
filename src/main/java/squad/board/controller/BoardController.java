package squad.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import squad.board.service.MemberService;

@Controller
@RequiredArgsConstructor
public class BoardController {
    // 게시판 화면
    @GetMapping("/boards")
    public String boardListView() {
        return "/board/boardList";
    }

    // 게시글 작성 화면
    @GetMapping("/boards/new")
    public String createBoard() {
        return "/board/createBoard";
    }
}
