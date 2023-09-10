package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.comment.Comment;
import squad.board.dto.comment.CommentListResponse;
import squad.board.dto.comment.CommentSaveRequest;
import squad.board.dto.comment.CommentUpdateRequest;
import squad.board.repository.CommentMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentMapper commentMapper;

    public CommonIdResponse saveComment(Long memberId, Long boardId, CommentSaveRequest commentSaveRequest) {
        Comment comment = commentSaveRequest.toEntity(memberId, boardId);
        return new CommonIdResponse(commentMapper.save(comment));
    }

    @Transactional(readOnly = true)
    public CommentListResponse getCommentList(Long boardId) {
        return new CommentListResponse(commentMapper.findAllCommentsWithNickName(boardId));
    }

    public void deleteComment(Long commentId, Long memberId) {
        commentMapper.deleteByCommentId(commentId);
    }

    public void updateComment(Long commentId, Long memberId, CommentUpdateRequest commentUpdateRequest) {
        commentMapper.updateByCommentId(commentId, commentUpdateRequest.getContent(), LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public CommentListResponse getChildComment(Long boardId, Long parentCommentId) {
        return new CommentListResponse(commentMapper.findAllChildComments(boardId, parentCommentId));
    }

}
