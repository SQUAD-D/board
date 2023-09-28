package squad.board.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import squad.board.aop.CommentWriterAuth;
import squad.board.argumentresolver.SessionAttribute;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.dto.ContentListResponse;
import squad.board.dto.comment.CommentResponse;
import squad.board.dto.comment.CommentSaveRequest;
import squad.board.dto.comment.CommentUpdateRequest;
import squad.board.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class CommentApiController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/{boardId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonIdResponse saveComment(
            @SessionAttribute Long memberId,
            @PathVariable Long boardId,
            @RequestBody @Valid CommentSaveRequest commentSaveRequest
    ) {
        return commentService.saveComment(memberId, boardId, commentSaveRequest);
    }

    // 전체 댓글 리스트
    @GetMapping("/{boardId}/comments")
    public ContentListResponse<CommentResponse> findAllComments(
            @PathVariable Long boardId,
            @RequestParam Long size,
            @RequestParam Long page
    ) {
        return commentService.getCommentList(boardId, size, page, null);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @CommentWriterAuth
    public void deleteComment(@PathVariable Long commentId, @SessionAttribute Long memberId) {
        commentService.deleteComment(commentId, memberId);
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    @CommentWriterAuth
    public void updateComment(
            @PathVariable Long commentId,
            @SessionAttribute Long memberId,
            @RequestBody CommentUpdateRequest commentUpdateRequest) {
        commentService.updateComment(commentId, memberId, commentUpdateRequest);
    }

    // 대댓글 리스트
    @GetMapping("/{boardId}/childComments/{parentCommentId}")
    public ContentListResponse<CommentResponse> getChildComment(
            @PathVariable Long boardId,
            @PathVariable Long parentCommentId
    ) {
        return commentService.getChildComment(boardId, parentCommentId);
    }
}
