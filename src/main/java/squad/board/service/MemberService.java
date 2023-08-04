package squad.board.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.domain.member.Member;
import squad.board.dto.member.CreateMemberDto;
import squad.board.dto.member.LoginMemberDto;
import squad.board.repository.MemberMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberMapper memberMapper;

    @Transactional
    public Long join(CreateMemberDto createMemberDto){
        Member member = createMemberDto.toEntity();
        memberMapper.save(member);
        return member.getMemberId();
    }

    // Id로 회원 조회
    public Member findMember(Long memberId){
        return memberMapper.findById(memberId);
    }

    // 회원 로그인
    public Member login(LoginMemberDto loginMemberDto){
        String loginId = loginMemberDto.getLoginId();
        String loginPw = loginMemberDto.getLoginPw();
        return memberMapper.findMemberByLoginIdAndLoginPw(loginId, loginPw);
    }

    // 로그인(세션) 검증
    public String sessionValidation(Member member, HttpServletRequest request){
        // 로그인 정보 검증
        if(member == null){
            return "failed";
        }
        // 기존 세션은 파기
        request.getSession().invalidate();
        // 세션이 없다면 새로운 세션 생성
        HttpSession session = request.getSession(true);
        // 세션에 member의 PK 값을 Value로 세팅
        session.setAttribute("memberId",member.getMemberId());
        // 유효 시간은 30분
        session.setMaxInactiveInterval(1800);

        return "success";
    }

    // 회원가입 시 중복아이디 검증
    public String validationLoginId(String loginId){
        int result = memberMapper.findByLoginId(loginId);
        if(result > 0){
            return "failed";
        }
        return "success";
    }
}
