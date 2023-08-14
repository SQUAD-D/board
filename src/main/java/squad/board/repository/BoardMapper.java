package squad.board.repository;

import org.apache.ibatis.annotations.Mapper;
import squad.board.domain.board.Board;

@Mapper
public interface BoardMapper {
    // 게시글 저장
    Long save(Board board);
}
