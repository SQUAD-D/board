package squad.board.dto.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BoardDetailResponse {
    private Long boardId;
    private String title;
    private String content;
    private String nickName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
