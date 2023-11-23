package squad.board.apicontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import squad.board.argumentresolver.SessionAttribute;
import squad.board.dto.ContentListResponse;
import squad.board.dto.board.BoardResponse;
import squad.board.dto.comment.CommentResponse;
import squad.board.service.BoardService;
import squad.board.service.CommentService;

import java.util.Optional;

@RestController
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class MyPageApiController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/my-boards")
    public ContentListResponse<BoardResponse> myBoardList(
            @RequestParam Long size,
            @RequestParam Long page,
            @SessionAttribute Long memberId
    ) {
        return boardService.findBoards(size, page, Optional.ofNullable(memberId));
    }

    // 나의 댓글 리스트
    @GetMapping("/my-comments")
    public ContentListResponse<CommentResponse> myCommentList(
            @RequestParam Long size,
            @RequestParam Long page,
            @SessionAttribute Long memberId
    ) {
        return commentService.getCommentList(null, size, page, memberId);
    }
}
