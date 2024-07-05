package squad.board.dto.S3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class S3Task {
    private String imgUUID;
    private Integer failCount = 0;

    public void increaseFailCount() {
        this.failCount++;
    }
}
