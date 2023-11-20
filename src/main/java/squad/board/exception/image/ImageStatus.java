package squad.board.exception.image;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import squad.board.commonresponse.CommonStatus;

@Getter
public enum ImageStatus implements CommonStatus {
    IMAGE_SIZE_EXCEEDED(HttpStatus.valueOf(400), 500, "[이미지 크기 초과]이미지 파일 허용 크기는 5MB입니다."),
    IMAGE_NAME_SIZE_EXCEEDED(HttpStatus.valueOf(400), 501, "이미지명은 100자를 초과할 수 없습니다."),
    INVALID_IMAGE_EXTENSION(HttpStatus.valueOf(400), 502, "업로드가 불가능한 이미지 확장자입니다.");
    private final HttpStatus httpStatusCode;
    private final int code;
    private final String message;

    ImageStatus(HttpStatus httpStatusCode, int code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
