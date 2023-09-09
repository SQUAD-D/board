package squad.board.repository;

import org.apache.ibatis.annotations.Mapper;
import squad.board.domain.comment.Comment;
import squad.board.dto.comment.CommentResponse;

import java.util.List;

@Mapper
public interface CommentMapper {
    Long save(Comment comment);

    List<CommentResponse> findAllCommentsWithNickName(Long boardId);

    void deleteByBoardId(Long boardId);

    void deleteByCommentId(Long commentId);

    Comment findByCommentId(Long commentId);

    void updateByCommentId(Long commentId, String content);
}
