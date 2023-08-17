package squad.board.apicontroller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.commonresponse.CommonResponse;
import squad.board.dto.board.BoardUpdateRequest;
import squad.board.dto.board.CreateBoard;
import squad.board.exception.board.BoardStatus;
import squad.board.service.BoardService;
import squad.board.service.MemberService;

import static squad.board.exception.board.BoardStatus.*;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final MemberService memberService;
    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("/boards/new")
    public CommonResponse<CommonIdResponse, BoardStatus> saveBoard(
            HttpServletRequest request,
            @RequestBody CreateBoard createBoard) {
        Long memberId = memberService.validateSession(request);
        return new CommonResponse<>(SUCCESS, boardService.createBoard(memberId, createBoard));
    }

    // 게시글 삭제
    @DeleteMapping("/boards/{boardId}")
    public CommonResponse<CommonIdResponse, BoardStatus> deleteBoard(
            HttpServletRequest request,
            @PathVariable Long boardId
    ) {
        Long memberId = memberService.validateSession(request);
        return new CommonResponse<>(DELETE_SUCCESS, boardService.deleteBoard(boardId, memberId));
    }

    @PatchMapping("/boards/{boardId}")
    public CommonResponse<CommonIdResponse, BoardStatus> updateBoard(
            HttpServletRequest request,
            @PathVariable Long boardId,
            @RequestBody BoardUpdateRequest boardUpdateRequest
    ) {
        memberService.validateSession(request);
        return new CommonResponse<>(UPDATE_SUCCESS, boardService.updateBoard(boardId, boardUpdateRequest));
    }
}
