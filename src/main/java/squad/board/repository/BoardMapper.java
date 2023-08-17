package squad.board.repository;

import org.apache.ibatis.annotations.Mapper;
import squad.board.domain.board.Board;
import squad.board.dto.board.BoardDetailResponse;
import squad.board.dto.board.BoardResponse;
import squad.board.dto.board.BoardUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BoardMapper {
    // 게시글 저장
    Long save(Board board);

    // 전체 게시글 조회 [member join for nickname]
    List<BoardResponse> findAllWithNickName();

    // 상세게시글 조회 [member join for nickname]
    BoardDetailResponse findByIdWithNickName(Long boardId);

    // 게시글 삭제
    void deleteById(Long boardId);

    // 게시글 단건 조회
    Board findById(Long boardId);

    // 게시글 업데이트
    void updateById(Long boardId, BoardUpdateRequest dto, LocalDateTime modifiedDate);
}
