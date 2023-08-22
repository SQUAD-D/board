package squad.board.dto.board;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import squad.board.domain.board.Board;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CreateBoardRequest {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    public Board toEntity(Long memberId) {
        return Board.builder()
                .memberId(memberId)
                .title(this.title)
                .content(this.content)
                .createdDate(LocalDateTime.now())
                .modifiedDate(null)
                .build();
    }
}
