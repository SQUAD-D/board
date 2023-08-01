package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.domain.member.Member;
import squad.board.dto.member.CreateMemberDto;
import squad.board.dto.member.LoginMemberDto;
import squad.board.repository.MemberMapper;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;

    @Transactional
    public void join(CreateMemberDto createMemberDto){
        Member member = createMemberDto.toEntity();
        memberMapper.save(member);
    }

    public Member findMember(Long memberId){
        return memberMapper.findById(memberId);
    }

    public Member login(LoginMemberDto loginMemberDto){
        String loginId = loginMemberDto.getLoginId();
        String loginPw = loginMemberDto.getLoginPw();
        return memberMapper.findMemberByLoginIdAndLoginPw(loginId, loginPw);
    }
}
