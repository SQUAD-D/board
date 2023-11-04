package squad.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
import squad.board.repository.ImageMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {

    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;
    private final ImageMapper imageMapper;
    private final S3Service s3Service;

    public CommonIdResponse createBoard(Long memberId, CreateBoardRequest createBoard, MultipartFile image) {
        Board board = createBoard.toEntity(memberId);
        boardMapper.save(board);
        String imageName;
        // TODO : 다 끝나고 IOE 처리하자
        if (image != null) {
            try {
                imageName = s3Service.saveFile(image);
                imageMapper.save(board.getBoardId(), imageName);
            } catch (IOException e) {
            }
        }

        return new CommonIdResponse(board.getBoardId());
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
        BoardDetailResponse response = boardMapper.findByIdWithNickName(boardId);
        String imageFileName = imageMapper.findImageFileName(boardId);
        response.setImageURL(s3Service.loadImage(imageFileName));
        return response;
    }

    public CommonIdResponse deleteBoard(Long boardId, Long memberId) {
        String imageFileName = imageMapper.findImageFileName(boardId);
        s3Service.deleteImage(imageFileName);
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
        Pagination boardPaging = new Pagination(requestPage, boardMapper.countByKeyWord(keyWord, searchType), size);
        List<BoardResponse> byKeyWord = boardMapper.findByKeyWord(keyWord, size, offset, searchType);
        return new ContentListResponse<>(boardMapper.findByKeyWord(keyWord, size, offset, searchType), boardPaging);
    }

    private Long calcOffset(Long page, Long size) {
        return (page - 1) * size;
    }
}
