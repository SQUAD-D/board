package squad.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
// 상세 게시글 DTO
public class BoardDetailResponse {
    private Long boardId;
    private String title;
    private String content;
    private String nickName;
    private String createdDate;
    private String modifiedDate;
}
