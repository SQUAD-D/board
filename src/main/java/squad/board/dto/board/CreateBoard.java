package squad.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import squad.board.domain.board.Board;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CreateBoard {
    private String title;
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
