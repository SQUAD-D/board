package squad.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class BoardServiceTest {

    @Test
    @DisplayName("작성자가 아닌 회원이 삭제요청 시 예외를 발생시킨다.")
    void invalidDeleteRequest() {
        //give

        //when

        //then
    }

}