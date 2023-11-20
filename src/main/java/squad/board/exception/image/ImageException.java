package squad.board.exception.image;

import squad.board.commonresponse.CommonStatus;
import squad.board.exception.CommonException;

public class ImageException extends CommonException {
    public ImageException(CommonStatus status) {
        super(status);
    }
}
