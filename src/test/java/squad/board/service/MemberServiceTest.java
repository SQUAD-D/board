package squad.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import squad.board.domain.member.Member;
import squad.board.dto.member.CreateMemberRequest;
import squad.board.exception.login.MemberException;
import squad.board.repository.MemberMapper;

@SpringBootTest
@Transactional
@Rollback
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberMapper memberMapper;

    @Test
    @DisplayName("저장한 멤버의 PK값으로 조회한 멤버의 PK는 같아야한다.")
    void 회원가입() {
        //given
        CreateMemberRequest createMember = new CreateMemberRequest("bukak2", "1234", "song", "hae");

        //when
        Long savedMemberId = memberService.join(createMember);
        Member findMember = memberService.findMember(savedMemberId);

        //then
        Assertions.assertThat(findMember.getMemberId()).isEqualTo(savedMemberId);
    }

    @Test
    @DisplayName("중복아이디는 회원가입을 승인하지 않는다.")
    void 중복아이디검증() {
        //given
        CreateMemberRequest createMember = new CreateMemberRequest("bukak3", "1234", "song", "hae");
        memberService.join(createMember);
        String loginId = "bukak3";

        //then
        Assertions.assertThatThrownBy(() -> memberService.validationMemberInfo(loginId))
                .isInstanceOf(MemberException.class);
    }

    @Test
    @DisplayName("발급된 세션에는 회원의 PK값이 있어야한다.")
    void 세션발급() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        Member member = memberService.findMember(80L);
        memberService.provideSession(member, request);

        //when
        Long memberId = (Long) request.getSession().getAttribute("memberId");

        //then
        Assertions.assertThat(memberId).isEqualTo(member.getMemberId());
    }
}