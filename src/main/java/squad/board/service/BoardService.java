package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.board.Board;
import squad.board.dto.board.BoardDetailResponse;
import squad.board.dto.board.BoardResponse;
import squad.board.dto.board.BoardUpdateRequest;
import squad.board.dto.board.CreateBoardRequest;
import squad.board.exception.board.BoardException;
import squad.board.repository.BoardMapper;

import java.time.LocalDateTime;
import java.util.List;

import static squad.board.exception.board.BoardStatus.INVALID_DELETE_UPDATE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardMapper boardMapper;

    @Transactional
    public CommonIdResponse createBoard(Long memberId, CreateBoardRequest createBoard) {
        Board board = createBoard.toEntity(memberId);
        Long boardId = boardMapper.save(board);
        return new CommonIdResponse(boardId);
    }

    public List<BoardResponse> findBoards() {
        return boardMapper.findAllWithNickName();
    }
    
    public BoardDetailResponse findOneBoard(Long boardId) {
        return boardMapper.findByIdWithNickName(boardId);
    }

    @Transactional
    public CommonIdResponse deleteBoard(Long boardId, Long memberId) {
        isOriginalWriter(boardId, memberId);
        boardMapper.deleteById(boardId);
        return new CommonIdResponse(boardId);
    }

    @Transactional
    public CommonIdResponse updateBoard(Long boardId, BoardUpdateRequest dto) {
        LocalDateTime modifiedDate = LocalDateTime.now();
        boardMapper.updateById(boardId, dto, modifiedDate);
        return new CommonIdResponse(boardId);
    }

    public void isOriginalWriter(Long boardId, Long memberId) {
        Board board = boardMapper.findById(boardId);
        if (!board.getMemberId().equals(memberId)) {
            throw new BoardException(INVALID_DELETE_UPDATE);
        }
    }
}
