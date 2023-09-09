package squad.board.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import squad.board.argumentresolver.SessionAttribute;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.dto.board.BoardDetailResponse;
import squad.board.dto.board.BoardUpdateRequest;
import squad.board.dto.board.CreateBoardRequest;
import squad.board.service.BoardService;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    // 게시글 생성
    @PostMapping
    public CommonIdResponse saveBoard(
            @SessionAttribute Long memberId,
            @Valid @RequestBody CreateBoardRequest createBoard) {
        return boardService.createBoard(memberId, createBoard);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public CommonIdResponse deleteBoard(
            @SessionAttribute Long memberId,
            @PathVariable Long boardId
    ) {
        return boardService.deleteBoard(boardId, memberId);
    }

    // 게시글 수정
    @PatchMapping("/{boardId}")
    // 반환고민
    public CommonIdResponse updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardUpdateRequest boardUpdateRequest,
            @SessionAttribute Long memberId
    ) {
        return boardService.updateBoard(boardId, memberId, boardUpdateRequest);
    }

    // 상세 게시글 조회
    @GetMapping("/{boardId}")
    public BoardDetailResponse getBoard(
            @PathVariable Long boardId
    ) {
        return boardService.findOneBoard(boardId);
    }
}
