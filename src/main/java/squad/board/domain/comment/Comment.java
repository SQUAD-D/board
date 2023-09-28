package squad.board.domain.comment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    private Long commentId;
    private Long memberId;
    private Long boardId;
    private Long parentCommentId;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public Comment(Long memberId, Long boardId, Long parentId, String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.memberId = memberId;
        this.boardId = boardId;
        this.parentCommentId = parentId;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
