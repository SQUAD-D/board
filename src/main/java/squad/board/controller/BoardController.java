package squad.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import squad.board.dto.board.BoardDetailResponse;
import squad.board.dto.board.BoardResponse;
import squad.board.service.BoardService;
import squad.board.service.MemberService;

import java.util.List;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;

    // 게시판 화면
    @GetMapping
    public String boardListView(HttpServletRequest request, Model model) {
        memberService.validateSession(request);
        List<BoardResponse> boards = boardService.findBoards();
        model.addAttribute("boards", boards);
        return "/board/boardList";
    }

    // 게시글 작성 화면
    @GetMapping("/new")
    public String createBoard(HttpServletRequest request) {
        memberService.validateSession(request);
        return "/board/createBoard";
    }

    // 상세 게시글 화면
    @GetMapping("/{boardId}")
    public String detailBoard(
            HttpServletRequest request,
            @PathVariable Long boardId,
            Model model
    ) {
        memberService.validateSession(request);
        BoardDetailResponse board = boardService.findOneBoard(boardId);
        model.addAttribute("board", board);
        return "/board/boardDetail";
    }

    @GetMapping("/update/{boardId}")
    public String updateBoard(
            HttpServletRequest request,
            @PathVariable Long boardId,
            Model model
    ) {
        Long memberId = memberService.validateSession(request);
        boardService.isOriginalWriter(boardId, memberId);
        BoardDetailResponse board = boardService.findOneBoard(boardId);
        model.addAttribute("board", board);
        return "/board/boardUpdate";
    }
}
