package squad.board.apicontroller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.commonresponse.CommonResponse;
import squad.board.dto.board.CreateBoardDto;
import squad.board.exception.board.BoardStatus;
import squad.board.service.BoardService;
import squad.board.service.MemberService;

import static squad.board.exception.board.BoardStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final MemberService memberService;
    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("/boards/new")
    public CommonResponse<CommonIdResponse, BoardStatus> saveBoard(
            HttpServletRequest request,
            @RequestBody CreateBoardDto createBoardDto) {
        Long memberId = memberService.validateSession(request);
        return new CommonResponse<>(SUCCESS, boardService.createBoard(memberId, createBoardDto));
    }
}
