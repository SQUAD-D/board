package squad.board.repository;

import org.apache.ibatis.annotations.Mapper;
import squad.board.domain.board.Board;
import squad.board.dto.board.BoardResponseDto;

import java.util.List;

@Mapper
public interface BoardMapper {
    // 게시글 저장
    Long save(Board board);

    // 전체 게시글 조회
    // TODO : 페이징 처리 필요
    List<BoardResponseDto> findAllWithNickName();
}
