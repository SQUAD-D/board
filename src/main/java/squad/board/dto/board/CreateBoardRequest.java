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
@AllArgsConstructor
public class CreateBoardRequest {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    private List<ImageInfoRequest> imageInfo;

    // 이 메서드가 toEntity보다 먼저 실행돼야함 TODO 질문
    public void changeImageSrc(String from, String to) {
        // regex 를 이용해 from 폴더 -> to 폴더로 src 속성값 변경
        content = content.replace(".com/" + from + "/", ".com/" + to + "/");
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
