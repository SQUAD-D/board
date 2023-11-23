package squad.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
// 상세 게시글 DTO
public class BoardDetailResponse {
    private Long boardId;
    private String title;
    private String content;
    private String nickName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
