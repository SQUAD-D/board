package squad.board.apicontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import squad.board.aop.BoardWriterAuth;
import squad.board.argumentresolver.SessionAttribute;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.dto.ContentListResponse;
import squad.board.dto.board.*;
import squad.board.service.BoardService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    // 게시글 생성
    @PostMapping(value = "/boards")
    public CommonIdResponse saveBoard(
            @SessionAttribute Long memberId,
            @Valid @RequestBody CreateBoardRequest createBoard) throws JsonProcessingException {
        return boardService.createBoard(memberId, createBoard);
    }

//    @PostMapping(value = "/boards", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
//        MediaType.APPLICATION_JSON_VALUE})
//    public CommonIdResponse saveBoard(
//        @SessionAttribute Long memberId,
//        @RequestPart CreateBoardRequest createBoard,
//        @RequestPart("images") MultipartFile[] images) {
//        return boardService.createBoard(memberId, createBoard, images);
//    }

    // 이미지 S3 전송
    @PostMapping(value = "/boards/img")
    public ImageInfoResponse saveImg(
            @RequestPart(value = "image") MultipartFile image) {
        return boardService.saveImageToS3(image);
    }

    // 게시글 삭제
    @DeleteMapping("/boards/{boardId}")
    @BoardWriterAuth
    public CommonIdResponse deleteBoard(
            @PathVariable Long boardId
    ) {
        return boardService.deleteBoard(boardId);
    }

    // 게시글 수정
    @PatchMapping("/boards/{boardId}")
    @BoardWriterAuth
    public CommonIdResponse updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardUpdateRequest boardUpdateRequest
    ) {
        return boardService.updateBoard(boardId, boardUpdateRequest);
    }

    // 상세 게시글 조회
    @GetMapping("/boards/{boardId}")
    public BoardDetailResponse getBoard(
            @PathVariable Long boardId
    ) {
        return boardService.findOneBoard(boardId);
    }

    // 전체 게시글 조회 (페이징 처리)
    @GetMapping("/boards")
    public ContentListResponse<BoardResponse> boardList(
            @RequestParam Long size,
            @RequestParam Long page
    ) {
        return boardService.findBoards(size, page);
    }

    // 게시글 검색
    @GetMapping("/boards/search")
    public ContentListResponse<BoardResponse> searchBoard(
            @RequestParam String keyWord,
            @RequestParam Long size,
            @RequestParam Long page,
            @RequestParam String searchType
    ) {
        return boardService.searchBoard(keyWord, size, page, searchType);
    }
}
