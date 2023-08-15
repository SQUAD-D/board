package squad.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import squad.board.dto.board.BoardResponseDto;
import squad.board.service.BoardService;
import squad.board.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;

    // 게시판 화면
    @GetMapping("/boards")
    public String boardListView(HttpServletRequest request, Model model) {
        memberService.validateSession(request);
        List<BoardResponseDto> boards = boardService.findBoards();
        model.addAttribute("boards", boards);
        return "/board/boardList";
    }

    // 게시글 작성 화면
    @GetMapping("/boards/new")
    public String createBoard(HttpServletRequest request) {
        memberService.validateSession(request);
        return "/board/createBoard";
    }
}
