package squad.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ImageInfoRequest {
    private String imageUUID;
    private long imageSize;
    private String imageOriginalName;
}
