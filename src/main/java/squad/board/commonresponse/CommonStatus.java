package squad.board.commonresponse;

import org.springframework.http.HttpStatus;

public interface CommonStatus {
    HttpStatus getHttpStatusCode();

    int getCode();

    String getMessage();
}
