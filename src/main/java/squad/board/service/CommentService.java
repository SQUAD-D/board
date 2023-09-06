package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.aop.CommentWriterAuth;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.comment.Comment;
import squad.board.dto.comment.CommentListResponse;
import squad.board.dto.comment.CommentSaveRequest;
import squad.board.dto.comment.CommentUpdateRequest;
import squad.board.repository.CommentMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentMapper commentMapper;

    @Transactional
    public CommonIdResponse saveComment(Long memberId, Long boardId, CommentSaveRequest commentSaveRequest) {
        Comment comment = commentSaveRequest.toEntity(memberId, boardId);
        return new CommonIdResponse(commentMapper.save(comment));
    }

    public CommentListResponse getCommentList(Long boardId) {
        return new CommentListResponse(commentMapper.findAllCommentsWithNickName(boardId));
    }

    @Transactional
    @CommentWriterAuth
    public void deleteComment(Long commentId, Long memberId) {
        commentMapper.deleteByCommentId(commentId);
    }

    @Transactional
    @CommentWriterAuth
    public void updateComment(Long commentId, Long memberId, CommentUpdateRequest commentUpdateRequest) {
        commentMapper.updateByCommentId(commentId, commentUpdateRequest.getContent());
    }
}
