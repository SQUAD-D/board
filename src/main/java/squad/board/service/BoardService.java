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
import squad.board.exception.image.ImageException;
import squad.board.exception.image.ImageStatus;
import squad.board.repository.BoardMapper;
import squad.board.repository.CommentMapper;
import squad.board.repository.ImageMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {

    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;
    private final ImageMapper imageMapper;
    private final S3Service s3Service;
    private static final long MAX_IMAGE_SIZE = 5000000L;
    private static final int MAX_IMAGE_NAME_SIZE = 100;


    public CommonIdResponse createBoard(Long memberId, CreateBoardRequest createBoard) {
        // 게시글 저장
        Board board = createBoard.toEntity(memberId);
        boardMapper.save(board);
        // 이미지 정보 저장
        List<ImageInfoRequest> imageInfoRequests = createBoard.getImageInfo();
        if (!imageInfoRequests.isEmpty()) {
            // DB에 이미지 정보 저장
            saveImageInfo(board.getBoardId(), imageInfoRequests);
        }
        return new CommonIdResponse(board.getBoardId());
    }

    private void saveImageInfo(Long boardId, List<ImageInfoRequest> imageInfoRequests) {
        imageMapper.save(imageInfoRequests, boardId);
        for (ImageInfoRequest request : imageInfoRequests) {
            // tmp 폴더의 이미지를 original 폴더로 이동
            s3Service.moveImageToOriginal(request.getImageUUID(), "tmp", "original");
        }
    }

    public ImageInfoResponse saveImageToS3(MultipartFile image) {
        imageValidation(image);
        String uuid = s3Service.saveFile(image, "tmp");
        String imgSrc = s3Service.loadImage(uuid, "tmp");
        return new ImageInfoResponse(uuid, image.getSize(), image.getOriginalFilename(), imgSrc);
    }

    private static void imageValidation(MultipartFile image) {
        String imageName = image.getOriginalFilename();
        if (!imageName.substring(imageName.lastIndexOf(".")).matches("(.png|.jpg|.jpeg)$")) {
            throw new ImageException(ImageStatus.INVALID_IMAGE_EXTENSION);
        }
        // 이미지 파일명 길이 제한
        if (imageName.length() > MAX_IMAGE_NAME_SIZE) {
            throw new ImageException(ImageStatus.IMAGE_NAME_SIZE_EXCEEDED);
        }
        long imgSize = image.getSize();
        // 이미지 파일 크기 제한
        if (imgSize > MAX_IMAGE_SIZE) {
            throw new ImageException(ImageStatus.IMAGE_SIZE_EXCEEDED);
        }
    }

    @Transactional(readOnly = true)
    public ContentListResponse<BoardResponse> findBoards(Long size, Long requestPage, Optional<Long> memberId) {
        Long validMemberId = memberId.orElseThrow(() -> new BoardException(BoardStatus.INVALID_MEMBER_ID));
        Long offset = calcOffset(requestPage, size);
        Pagination boardPaging = new Pagination(requestPage, boardMapper.countBoards(validMemberId), size);
        return new ContentListResponse<>(boardMapper.findAllWithNickName(size, offset, validMemberId), boardPaging);
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
        List<String> imageUUID = imageMapper.findImageUuid(boardId);
        for (String uuid : imageUUID) {
            s3Service.deleteImage(uuid, "original");
        }
        imageMapper.deleteByBoardId(boardId);
        boardMapper.deleteById(boardId);
        commentMapper.deleteByBoardId(boardId);
        return new CommonIdResponse(boardId);
    }

    public CommonIdResponse updateBoard(Long boardId, BoardUpdateRequest updateBoard) {
        // 이미지 정보 저장
        Optional<List<ImageInfoRequest>> imageInfoRequests =
                Optional.ofNullable(updateBoard.getImageInfoList());
        // DB에 이미지 정보 저장
        imageInfoRequests.ifPresent(infoRequests -> saveImageInfo(boardId, infoRequests));
        // 기존 이미지 정보
        List<String> savedImageUuid = imageMapper.findImageUuid(boardId);
        // 이미지가 있는 게시글일 경우 check
        if (!savedImageUuid.isEmpty()) {
            List<String> deleteRequestImageUuid = updateBoard.checkDeletedImage(savedImageUuid);
            // 삭제 요청 이미지가 있을 경우
            if (!deleteRequestImageUuid.isEmpty()) {
                imageMapper.deleteByImageUuid(deleteRequestImageUuid);
            }
        }
        LocalDateTime modifiedDate = LocalDateTime.now();
        boardMapper.updateById(boardId, updateBoard, modifiedDate);
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
