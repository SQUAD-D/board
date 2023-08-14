package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.board.Board;
import squad.board.domain.member.Member;
import squad.board.dto.board.CreateBoardDto;
import squad.board.repository.BoardMapper;

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
}
