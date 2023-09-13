package squad.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import squad.board.dto.board.BoardDetailResponse;
import squad.board.dto.board.BoardResponse;
import squad.board.service.BoardService;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시판 리스트 페이지
    @GetMapping
    public String boardListView(
            @RequestParam Long page,
            @RequestParam Long size,
            Model model) {
        List<BoardResponse> boards = boardService.findBoards();
        model.addAttribute("boards", boards);
        model.addAttribute("pagination", boardPaging);
        return "/board/boardList";
    }

    // 게시글 작성 페이지
    @GetMapping("/new")
    public String createBoard() {
        return "/board/createBoard";
    }

    // 상세 게시글 페이지
    @GetMapping("/{boardId}")
    public String detailBoard(
            @PathVariable Long boardId,
            Model model
    ) {
        BoardDetailResponse board = boardService.findOneBoard(boardId);
        model.addAttribute("board", board);
        return "/board/detailBoard";
    }

    // 게시글 수정 페이지
    @GetMapping("/update/{boardId}")
    public String updateBoard(
    ) {
        return "/board/updateBoard";
    }
}
