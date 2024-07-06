package squad.board.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {

    private static final long MAX_IMAGE_SIZE = 5000000L;
    private static final int MAX_IMAGE_NAME_SIZE = 100;
    private static final String IMAGE_EXTENSION_EXTRACT_REGEX = "(.png|.jpg|.jpeg)$";
    private static final String TEMP_FOLDER_NAME = "tmp";
    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;
    private final ImageMapper imageMapper;
    private final S3Service s3Service;
    private final S3MessageQueue messageQueue;
    private final S3DeadLetterQueue deadLetterQueue;

    public CommonIdResponse createBoard(Long memberId, CreateBoardRequest createBoard) throws JsonProcessingException {
        // 게시글 저장
        Board board = createBoard.toEntity(memberId);
        boardMapper.save(board);
        // 이미지 정보 저장
        if (createBoard.isImageExist()) {
            imageMapper.save(createBoard.getImageInfo(), board.getBoardId());
            deadLetterQueue.pushAll(createBoard.getImageInfo().stream()
                    .map(ImageInfoRequest::getImageUUID)
                    .toList());
        }
        return new CommonIdResponse(board.getBoardId());
    }

    public ImageInfoResponse saveImageToS3(MultipartFile image) {
        imageValidation(image);
        String uuid = s3Service.saveFile(image, TEMP_FOLDER_NAME);
        String imgSrc = s3Service.loadImage(uuid, TEMP_FOLDER_NAME);
        return new ImageInfoResponse(uuid, image.getSize(), image.getOriginalFilename(), imgSrc);
    }

    private void imageValidation(MultipartFile image) {
        String imageName = image.getOriginalFilename();
        if (!imageName.substring(imageName.lastIndexOf("."))
                .matches(IMAGE_EXTENSION_EXTRACT_REGEX)) {
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
    public ContentListResponse<BoardResponse> findBoards(Long size, Long requestPage,
                                                         Long memberId) {
        if (memberId==null) {
            throw new BoardException(BoardStatus.INVALID_MEMBER_ID);
        }
        Long offset = calcOffset(requestPage, size);
        Pagination boardPaging = new Pagination(requestPage, boardMapper.countBoards(memberId),
                size);
        return new ContentListResponse<>(boardMapper.findAllWithNickName(size, offset, memberId),
                boardPaging);
    }

    private Long calcOffset(Long page, Long size) {
        return (page - 1) * size;
    }

    @Transactional(readOnly = true)
    public ContentListResponse<BoardResponse> findBoards(Long size, Long requestPage) {
        Long offset = calcOffset(requestPage, size);
        Pagination boardPaging = new Pagination(requestPage, boardMapper.countBoards(null), size);
        return new ContentListResponse<>(boardMapper.findAllWithNickName(size, offset, null),
                boardPaging);
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
        List<ImageInfoRequest> imageInfoRequests = updateBoard.getImageInfoList();
        // DB에 이미지 정보 저장
        if (CollectionUtils.isNotEmpty(imageInfoRequests)) {
            imageMapper.save(imageInfoRequests, boardId);
        }
        // 기존 이미지 정보
        List<String> savedImageUuid = imageMapper.findImageUuid(boardId);
        // 이미지가 있는 게시글일 경우 check
        if (CollectionUtils.isNotEmpty(savedImageUuid)) {
            List<String> deleteRequestImageUuid = updateBoard.checkDeletedImage(savedImageUuid);
            deleteImageInfo(deleteRequestImageUuid);
        }
        LocalDateTime modifiedDate = LocalDateTime.now();
        boardMapper.updateById(boardId, updateBoard, modifiedDate);
        return new CommonIdResponse(boardId);
    }

    private void deleteImageInfo(List<String> deleteRequestImageUuid) {
        if (CollectionUtils.isNotEmpty(deleteRequestImageUuid)) {
            imageMapper.deleteByImageUuid(deleteRequestImageUuid);
        }
    }

    @Transactional(readOnly = true)
    public ContentListResponse<BoardResponse> searchBoard(String keyWord, Long size,
                                                          Long requestPage, String searchType) {
        Long offset = calcOffset(requestPage, size);
        Pagination boardPaging = new Pagination(requestPage,
                boardMapper.countByKeyWord(keyWord, searchType), size);
        List<BoardResponse> byKeyWord = boardMapper.findByKeyWord(keyWord, size, offset,
                searchType);
        return new ContentListResponse<>(
                boardMapper.findByKeyWord(keyWord, size, offset, searchType), boardPaging);
    }
}
