package squad.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
// 상세 게시글 DTO
public class BoardDetailResponse {
    private Long boardId;
    private String title;
    private String content;
    private String nickName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String imageURL;

    public BoardDetailResponse(Long boardId, String title, String content, String nickName, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.nickName = nickName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
