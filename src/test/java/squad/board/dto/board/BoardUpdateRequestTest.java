package squad.board.dto.board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class BoardUpdateRequestTest {

    @Test
    @DisplayName("사용자의 Content에 존재하는 이미지와 DB에 존재하는 이미지가 불일치한다면 추출에 성공한다.")
    public void checkDeleteImage() {
        //given
        String imageUUID1 = UUID.randomUUID().toString();
        String imageUUID2 = UUID.randomUUID().toString();
        String imageUUID3 = UUID.randomUUID().toString();
        String content = "original/" + imageUUID1 + "\"" + "foo/" + "original/" + imageUUID2 + "\"" + "foo";
        List<ImageInfoRequest> imageInfoRequests = new ArrayList<>();
        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest("test", content, imageInfoRequests);
        List<String> originalUUID = List.of(imageUUID1, imageUUID2, imageUUID3);
        //when
        List<String> result = boardUpdateRequest.checkDeletedImage(originalUUID);
        //then
        System.out.println(result);
        Assertions.assertThat(result.get(0)).isEqualTo(imageUUID3);
    }

}