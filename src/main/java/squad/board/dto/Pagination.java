package squad.board.dto;

import lombok.Getter;
import lombok.Setter;
import squad.board.exception.board.BoardException;
import squad.board.exception.board.BoardStatus;

@Getter
public class Pagination {
    private final Long defaultPageSize = 10L;
    private Long totalContent;
    private Long totalPages;
    private Long currentPage;
    private Long firstPage;
    private Long lastPage;

    public Pagination(Long requestPage, Long totalContent, Long size) {
        this.currentPage = requestPage;
        this.totalContent = totalContent;
        this.totalPages = calculateTotalPages(size);
        // 페이지 범위를 벗어난 요청 예외처리
        if ((totalPages < requestPage || requestPage <= 0) && totalContent != 0) {
            throw new BoardException(BoardStatus.INVALID_PAGE_NUMBER);
        }
        calculatePageList();
    }

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
