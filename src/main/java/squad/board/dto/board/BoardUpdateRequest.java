package squad.board.dto.board;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardUpdateRequest {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
}
