package squad.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import squad.board.dto.S3.S3Task;

import javax.management.timer.TimerMBean;

@AllArgsConstructor
@Getter
@Setter
public class ImageInfoRequest {
    private String imageUUID;
    private long imageSize;
    private String imageOriginalName;

    public S3Task toS3Task() {
        return S3Task.builder()
                .imgUUID(imageUUID)
                .failCount(0)
                .build();
    }
}
