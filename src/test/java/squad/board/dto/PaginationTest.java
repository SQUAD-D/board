package squad.board.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import squad.board.exception.board.BoardException;

class PaginationTest {

    @Test
    @DisplayName("전체 게시글이 100건에 한 페이지당 10건의 게시글을 보여줘야한다면 총 페이지수는 10페이지다.")
    public void 총_페이지수연산() {
        //given
        Pagination pagination = new Pagination(1L, 100L, 10L);
        //when
        Long totalPageSize = pagination.calculateTotalPages(pagination.getRequestPageSize());
        //then
        Assertions.assertThat(totalPageSize).isEqualTo(10);
    }

    @Test
    @DisplayName("전체 게시글이 101건에 한 페이지당 10건의 게시글을 보여줘야한다면 총 페이지수는 11페이지다.")
    public void 총_페이지수연산2() {
        //given
        Pagination pagination = new Pagination(1L, 101L, 10L);
        //when
        Long totalPageSize = pagination.calculateTotalPages(pagination.getRequestPageSize());
        //then
        Assertions.assertThat(totalPageSize).isEqualTo(11);
    }

    @Test
    @DisplayName("총 페이지 수를 벗어난 요청은 예외를 발생시킨다.")
    public void 페이지_접근_예외() {
        // Pagination 은 존재하지않는 페이지 정보
        Assertions.assertThatExceptionOfType(BoardException.class).isThrownBy(() -> new Pagination(5L, 10L, 5L));
    }

}