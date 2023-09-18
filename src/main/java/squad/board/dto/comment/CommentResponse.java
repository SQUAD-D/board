package squad.board.dto.comment;

import lombok.Getter;

@Getter
public class CommentResponse {
    private Long commentId;
    private Long parentCommentId;
    private String content;
    private String createdDate;
    private String modifiedDate;
    private String nickName;
}
