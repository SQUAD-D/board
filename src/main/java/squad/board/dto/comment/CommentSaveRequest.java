package squad.board.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import squad.board.domain.comment.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentSaveRequest {
    @NotEmpty(message = "댓글 내용을 입력해주세요.")
    private String content;
    private Long parentCommentId;

    public Comment toEntity(Long memberId, Long boardId) {
        return Comment.builder()
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
