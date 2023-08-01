package squad.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import squad.board.domain.member.Member;
import squad.board.dto.member.CreateMemberDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    @DisplayName("MyBatis를 이용한 회원가입 성공여부 테스트")
    void joinTest(){
        CreateMemberDto createMemberDto = new CreateMemberDto("bukak2","1234","song","hae");
        memberService.join(createMemberDto);

        Member findMember = memberService.findMember(member.getMemberId());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
    }

}