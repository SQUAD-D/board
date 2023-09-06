package squad.board.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import squad.board.argumentresolver.SessionAttribute;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.dto.comment.CommentListResponse;
import squad.board.dto.comment.CommentSaveRequest;
import squad.board.dto.comment.CommentUpdateRequest;
import squad.board.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonIdResponse saveComment(
            @SessionAttribute Long memberId,
            @PathVariable Long boardId,
            @RequestBody @Valid CommentSaveRequest commentSaveRequest
    ) {
        return commentService.saveComment(memberId, boardId, commentSaveRequest);
    }

    @GetMapping
    public CommentListResponse findAllComments(
            @PathVariable Long boardId
    ) {
        return commentService.getCommentList(boardId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @SessionAttribute Long memberId) {
        commentService.deleteComment(commentId, memberId);
    }

    @PatchMapping("/{commentId}")
    public void updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest commentUpdateRequest,
            @SessionAttribute Long memberId) {
        commentService.updateComment(commentId, memberId, commentUpdateRequest);
    }
}
