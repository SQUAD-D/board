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
import squad.board.dto.board.*;
import squad.board.exception.board.BoardException;
import squad.board.exception.board.BoardStatus;
import squad.board.repository.BoardMapper;
import squad.board.repository.CommentMapper;
import squad.board.repository.ImageMapper;

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

    public CommonIdResponse createBoard(Long memberId, CreateBoardRequest createBoard) {
        // s3에서 이미지의 디렉토리를 옮기기때문에 사용자의 이미지 소스 url 도 변경
        createBoard.changeImageSrc("tmp", "original");
        // 게시글 저장
        Board board = createBoard.toEntity(memberId);
        boardMapper.save(board);
        // 이미지 정보 저장
        List<ImageInfoRequest> imageInfoRequests = createBoard.getImageInfo();
        for (ImageInfoRequest request : imageInfoRequests) {
            // DB에 이미지 정보 저장 TODO Bulk 연산
            imageMapper.save(request, board.getBoardId());
            // tmp 폴더의 이미지를 original 폴더로 이동
            s3Service.moveImageToOriginal(request.getImageUUID(), "tmp", "original");
        }
        return new CommonIdResponse(board.getBoardId());
    }

    public ImageInfoResponse saveImage(MultipartFile image) {
        // TODO 파일 이름 및 확장자 예외 처리가 필요함
        String imgOriginalName = image.getOriginalFilename();
        // TODO 파일 크기 예외 처리가 필요함
        long imgSize = image.getSize();
        String uuid = s3Service.saveFile(image, "tmp");
        String imgSrc = s3Service.loadImage(uuid, "tmp");
        return new ImageInfoResponse(uuid, imgSize, imgOriginalName, imgSrc);
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

    public CommonIdResponse deleteBoard(Long boardId) {
        List<String> imageUUID = imageMapper.findImageUUID(boardId);
        for (String uuid : imageUUID) {
            s3Service.deleteImage(uuid, "original");
        }
        boardMapper.deleteById(boardId);
        commentMapper.deleteByBoardId(boardId);
        return new CommonIdResponse(boardId);
    }

    public CommonIdResponse updateBoard(Long boardId, BoardUpdateRequest dto) {
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
