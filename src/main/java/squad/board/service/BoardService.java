package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.board.Board;
import squad.board.dto.Pagination;
import squad.board.dto.board.*;
import squad.board.exception.board.BoardException;
import squad.board.exception.board.BoardStatus;
import squad.board.repository.BoardMapper;
import squad.board.repository.CommentMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;

    public CommonIdResponse createBoard(Long memberId, CreateBoardRequest createBoard) {
        Board board = createBoard.toEntity(memberId);
        Long boardId = boardMapper.save(board);
        return new CommonIdResponse(boardId);
    }

    @Transactional(readOnly = true)
    public BoardListResponse findBoards(Long size, Long page) {
        Long offset = calcOffset(page, size);
        Pagination boardPaging = calcPages(boardMapper.countBoards(), size, page);
        return new BoardListResponse(boardMapper.findAllWithNickName(size, offset), boardPaging);
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
    public BoardListResponse searchBoard(String keyWord, Long size, Long page) {
        Long offset = calcOffset(page, size);
        Long boards = boardMapper.countByKeyWord(keyWord);
        if (boards.equals(0L)) {
            throw new BoardException(BoardStatus.INVALID_KEY_WORD);
        }
        Pagination boardPaging = calcPages(boardMapper.countByKeyWord(keyWord), size, page);
        return new BoardListResponse(boardMapper.findByKeyWord(keyWord, size, offset), boardPaging);
    }

    private Pagination calcPages(Long totalContent, Long size, Long page) {
        Pagination boardPaging = new Pagination();
        boardPaging.setCurrentPage(page);
        // 전체 게시글 수
        boardPaging.setTotalContent(totalContent);
        // 페이지 수 계산
        Long maxPage = boardPaging.calculateTotalPages(size);
        // 페이지 범위를 벗어난 요청 예외처리
        if (maxPage < page || page <= 0) {
            throw new BoardException(BoardStatus.INVALID_PAGE_NUMBER);
        }
        // 한 번에 보여줄 페이지 수 계산
        boardPaging.calculatePageList();
        return boardPaging;
    }

    private Long calcOffset(Long page, Long size) {
        return (page - 1) * size;
    }
}
