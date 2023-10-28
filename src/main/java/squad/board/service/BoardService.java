package squad.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.board.Board;
import squad.board.dto.ContentListResponse;
import squad.board.dto.Pagination;
import squad.board.dto.board.BoardDetailResponse;
import squad.board.dto.board.BoardResponse;
import squad.board.dto.board.BoardUpdateRequest;
import squad.board.dto.board.CreateBoardRequest;
import squad.board.exception.board.BoardException;
import squad.board.exception.board.BoardStatus;
import squad.board.repository.BoardMapper;
import squad.board.repository.CommentMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {

    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;

    public CommonIdResponse createBoard(Long memberId, CreateBoardRequest createBoard) {
        Board board = createBoard.toEntity(memberId);
        Long boardId = boardMapper.save(board);
        return new CommonIdResponse(boardId);
    }

    @Transactional(readOnly = true)
    public ContentListResponse<BoardResponse> findBoards(Long size, Long requestPage, Long memberId) {
        if (memberId == null) {
            log.info("memberId = {}");
            throw new BoardException(BoardStatus.INVALID_MEMBER_ID);
        }
        Long offset = calcOffset(requestPage, size);
        Pagination boardPaging = new Pagination(requestPage, boardMapper.countBoards(memberId), size);
        return new ContentListResponse<>(boardMapper.findAllWithNickName(size, offset, memberId), boardPaging);
    }

    @Transactional(readOnly = true)
    public ContentListResponse<BoardResponse> findBoards(Long size, Long requestPage) {
        Long offset = calcOffset(requestPage, size);
        Pagination boardPaging = new Pagination(requestPage, boardMapper.countBoards(null), size);
        return new ContentListResponse<>(boardMapper.findAllWithNickName(size, offset, null), boardPaging);
    }

    @Transactional(readOnly = true)
    public BoardDetailResponse findOneBoard(Long boardId) {
        return boardMapper.findByIdWithNickName(boardId);
    }

    public CommonIdResponse deleteBoard(Long boardId, Long memberId) {
        boardMapper.deleteById(boardId);
        commentMapper.deleteByBoardId(boardId);
        return new CommonIdResponse(boardId);
    }

    public CommonIdResponse updateBoard(Long boardId, Long memberId, BoardUpdateRequest dto) {
        LocalDateTime modifiedDate = LocalDateTime.now();
        boardMapper.updateById(boardId, dto, modifiedDate);
        return new CommonIdResponse(boardId);
    }

    @Transactional(readOnly = true)
    public ContentListResponse<BoardResponse> searchBoard(String keyWord, Long size, Long requestPage, String searchType) {
        Long offset = calcOffset(requestPage, size);
        Pagination boardPaging = new Pagination(requestPage, boardMapper.countByKeyWord(keyWord), size);
        List<BoardResponse> byKeyWord = boardMapper.findByKeyWord(keyWord, size, offset, searchType);
        return new ContentListResponse<>(boardMapper.findByKeyWord(keyWord, size, offset, searchType), boardPaging);
    }

    private Long calcOffset(Long page, Long size) {
        return (page - 1) * size;
    }
}
