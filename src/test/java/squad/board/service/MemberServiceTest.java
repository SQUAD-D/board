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
import squad.board.repository.MemberMapper;

@SpringBootTest
@Transactional
@Rollback
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberMapper memberMapper;

    @Test
    @DisplayName("저장한 멤버의 PK값으로 조회한 멤버의 PK는 같아야한다.")
    void joinTest(){
        //given
        CreateMemberDto createMemberDto = new CreateMemberDto("bukak2","1234","song","hae");

        //when
        Long savedMemberId = memberService.join(createMemberDto);
        Member findMember = memberService.findMember(savedMemberId);

        //then
        Assertions.assertThat(findMember.getMemberId()).isEqualTo(savedMemberId);
    }
    
    @Test
    @DisplayName("DB에 해당하는 로그인 아이디가 존재하면 failed를 반환해야한다.")
    void validateLoginIdFailed(){
        //given
        CreateMemberDto createMemberDto = new CreateMemberDto("bukak3","1234","song","hae");
        memberService.join(createMemberDto);
        String loginId = "bukak3";
        //when
        String result = memberService.validationLoginId(loginId);
        //then
        Assertions.assertThat(result).isEqualTo("failed");
    }

    @Test
    @DisplayName("DB에 해당하는 로그인 아이디가 존재하지않으면 success를 반환해야한다.")
    void validateLoginIdSuccess(){
        //given
        CreateMemberDto createMemberDto = new CreateMemberDto("bukak3","1234","song","hae");
        memberService.join(createMemberDto);
        String loginId = "bukak1";

        //when
        String result = memberService.validationLoginId(loginId);

        //then
        Assertions.assertThat(result).isEqualTo("success");
    }
}