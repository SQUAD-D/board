package squad.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BoardResponse {
    private Long boardId;
    private String title;
    private String content;
    private String nickName;
    private LocalDateTime createdDate;
}
