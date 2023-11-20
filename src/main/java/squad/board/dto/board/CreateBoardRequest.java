package squad.board.dto.board;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import squad.board.domain.board.Board;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateBoardRequest {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    private List<ImageInfoRequest> imageInfo;

    public CreateBoardRequest(String title, String content, List<ImageInfoRequest> imageInfo) {
        this.title = title;
        this.content = content.replace(".com/tmp/", ".com/original/");
        this.imageInfo = imageInfo;
    }

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
