package squad.board.dto.comment;

import lombok.Getter;
import lombok.Setter;
import squad.board.dto.Pagination;

import java.util.List;

@Getter
@Setter
public class CommentListResponse {
    private List<CommentResponse> comments;
    private Pagination commentPaging;

    public CommentListResponse(List<CommentResponse> comments, Pagination commentPaging) {
        this.comments = comments;
        this.commentPaging = commentPaging;
    }

    public CommentListResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }
}
