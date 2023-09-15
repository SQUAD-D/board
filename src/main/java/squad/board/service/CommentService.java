package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.comment.Comment;
import squad.board.dto.comment.CommentListResponse;
import squad.board.dto.Pagination;
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
    public CommentListResponse getCommentList(Long boardId, Long size, Long page) {
        Pagination commentPaging = new Pagination();
        commentPaging.setCurrentPage(page);
        // 게시글의 전체 댓글 수
        commentPaging.setTotalContent(commentMapper.countCommentsByBoardId(boardId));
        // 페이지 수 계산
        Long maxPage = commentPaging.calculateTotalPages(size);
        // 페이지 범위를 벗어난 요청 예외처리
        if (maxPage < page || page <= 0) {
            throw new CommentException(CommentStatus.INVALID_PAGE_NUMBER);
        }
        // offset 계산
        Long offset = (page - 1) * size;
        // 한 번에 보여줄 페이지 수 계산
        commentPaging.calculatePageList();
        return new CommentListResponse(commentMapper.findAllCommentsWithNickName(boardId, size, offset), commentPaging);
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
