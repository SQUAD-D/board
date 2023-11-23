package squad.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class ContentListResponse<Content> {
    private List<Content> contents;
    private Pagination pagination;

    public ContentListResponse(List<Content> contents, Pagination pagination) {
        this.contents = contents;
        this.pagination = pagination;
    }

    public ContentListResponse(List<Content> contents) {
        this.contents = contents;
    }
}
