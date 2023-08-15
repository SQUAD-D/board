package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.board.Board;
import squad.board.domain.member.Member;
import squad.board.dto.board.BoardResponseDto;
import squad.board.dto.board.CreateBoardDto;
import squad.board.repository.BoardMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardMapper boardMapper;

    @Transactional
    public CommonIdResponse createBoard(Long memberId, CreateBoardDto createBoardDto) {
        Board board = createBoardDto.toEntity(memberId);
        Long boardId = boardMapper.save(board);
        return new CommonIdResponse(boardId);
    }

    public List<BoardResponseDto> findBoards() {
        return boardMapper.findAllWithNickName();
    }
}
