package squad.board.dto.board;

import lombok.Getter;
import lombok.Setter;
import squad.board.dto.Pagination;

import java.util.List;

@Getter
@Setter
public class BoardListResponse {
    private List<BoardResponse> boards;
    private Pagination boardPaging;

    public BoardListResponse(List<BoardResponse> boards, Pagination boardPaging) {
        this.boards = boards;
        this.boardPaging = boardPaging;
    }
}
