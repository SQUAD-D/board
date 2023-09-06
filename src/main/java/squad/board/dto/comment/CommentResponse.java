package squad.board.dto.comment;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private Long commentId;
    private Long parentCommentId;
    private String content;
    private String createdDate;
    private String modifiedDate;
    private String nickName;
}
