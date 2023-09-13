package squad.board.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentPaging {
    private Long totalComments;
    private Long totalPages;
    private Long currentPage;
    private Long firstPage;
    private Long lastPage;

    public Long calculateTotalPages(Long size) {
        long result = totalComments / size;
        if (totalComments % size != 0) {
            result += 1;
        }
        this.totalPages = result;
        return totalPages;
    }

    public void calculatePageList(Long defaultPageSize) {
        if (currentPage % defaultPageSize == 1) {
            firstPage = currentPage;
            lastPage = currentPage + defaultPageSize;
        } else {
            firstPage = currentPage;
            lastPage = currentPage + defaultPageSize;
        }
    }
}
