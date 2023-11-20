package squad.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImageInfoResponse {
    private String imageUUID;
    private long imageSize;
    private String imageOriginalName;
    private String imageSrc;
}
