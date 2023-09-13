package squad.board.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentListResponse {
    private List<CommentResponse> comments;
    private CommentPaging commentPaging;

    public CommentListResponse(List<CommentResponse> comments, CommentPaging commentPaging) {
        this.comments = comments;
        this.commentPaging = commentPaging;
    }

    public CommentListResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }
}
