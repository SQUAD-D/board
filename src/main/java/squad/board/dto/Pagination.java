package squad.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {
    private final Long defaultPageSize = 10L;
    private Long totalContent;
    private Long totalPages;
    private Long currentPage;
    private Long firstPage;
    private Long lastPage;

    // 총 페이지 수 연산
    public Long calculateTotalPages(Long size) {
        long result = totalContent / size;
        if (totalContent % size != 0) {
            result += 1;
        }
        this.totalPages = result;
        return totalPages;
    }

    public void calculatePageList() {
        final long diffFirstAndLast = defaultPageSize - 1;

        // 첫 페이지 연산 로직
        long currentPageQuotient = currentPage / defaultPageSize;
        long currentPageRemainder = currentPage % defaultPageSize;
        this.firstPage = (currentPageRemainder == 1) ? currentPage : currentPage - (currentPageRemainder - 1);

        if (currentPageRemainder == 0) {
            this.firstPage = defaultPageSize * currentPageQuotient - diffFirstAndLast;
        }

        // 마지막 페이지 연산 로직
        long totalPagesQuotient = totalPages / defaultPageSize;
        long totalPagesRemainder = totalPages % defaultPageSize;
        this.lastPage = (totalPagesQuotient == 0) ? totalPages : firstPage + defaultPageSize - 1;

        if (lastPage > totalPages) {
            this.lastPage = firstPage + totalPagesRemainder - 1;
        }
    }
}
