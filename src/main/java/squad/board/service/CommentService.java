package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.comment.Comment;
import squad.board.dto.ContentListResponse;
import squad.board.dto.Pagination;
import squad.board.dto.comment.CommentResponse;
import squad.board.dto.comment.CommentSaveRequest;
import squad.board.dto.comment.CommentUpdateRequest;
import squad.board.exception.comment.CommentException;
import squad.board.exception.comment.CommentStatus;
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
    public ContentListResponse<CommentResponse> getCommentList(Long boardId, Long size, Long requestPage, Long memberId) {
        Pagination commentPaging = new Pagination(
                requestPage,
                commentMapper.countCommentsByBoardId(boardId, memberId),
                size);
        // offset 계산
        Long offset = (requestPage - 1) * size;
        return new ContentListResponse<>(commentMapper.findAllCommentsWithNickName(boardId, size, offset, memberId), commentPaging);
    }

    public void deleteComment(Long commentId, Long memberId) {
        commentMapper.deleteByCommentId(commentId);
    }

    public void updateComment(Long commentId, Long memberId, CommentUpdateRequest commentUpdateRequest) {
        commentMapper.updateByCommentId(commentId, commentUpdateRequest.getContent(), LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public ContentListResponse<CommentResponse> getChildComment(Long boardId, Long parentCommentId) {
        return new ContentListResponse<>(commentMapper.findAllChildComments(boardId, parentCommentId));
    }

}
