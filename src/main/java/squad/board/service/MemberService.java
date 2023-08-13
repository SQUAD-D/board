package squad.board.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.domain.member.Member;
import squad.board.dto.member.CreateMemberDto;
import squad.board.dto.member.LoginRequestDto;
import squad.board.dto.member.LoginResponseDto;
import squad.board.exception.login.LoginException;
import squad.board.repository.MemberMapper;

import static squad.board.exception.login.LoginStatus.DUPLICATED_LOGIN_ID;
import static squad.board.exception.login.LoginStatus.INVALID_LOGIN_INFO;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberMapper memberMapper;

    @Transactional
    public Long join(CreateMemberDto createMemberDto) {
        Member member = createMemberDto.toEntity();
        memberMapper.save(member);
        return member.getMemberId();
    }

    // Id로 회원 조회
    public Member findMember(Long memberId) {
        return memberMapper.findById(memberId);
    }

    // 회원 로그인
    public Member login(LoginRequestDto loginRequestDto) {
        String loginId = loginRequestDto.getLoginId();
        String loginPw = loginRequestDto.getLoginPw();
        log.info("{} Login", loginId);
        return memberMapper.findMemberByLoginIdAndLoginPw(loginId, loginPw);
    }

    // 로그인(세션) 검증
    public LoginResponseDto sessionValidation(Member member, HttpServletRequest request) throws LoginException {
        // 로그인 정보 검증
        if (member == null) {
            throw new LoginException(INVALID_LOGIN_INFO);
        }
        // 기존 세션은 파기
        request.getSession().invalidate();
        // 세션이 없다면 새로운 세션 생성
        HttpSession session = request.getSession(true);
        // 세션에 member의 PK 값을 Value로 세팅
        session.setAttribute("memberId", member.getMemberId());
        // 유효 시간은 30분
        session.setMaxInactiveInterval(1800);

        return new LoginResponseDto(member.getMemberId(), member.getNickName());
    }

    // 회원가입 시 중복아이디 검증
    public void validationLoginId(String loginId) throws LoginException {
        Member findMember = memberMapper.findByLoginId(loginId);
        if (findMember != null) {
            throw new LoginException(DUPLICATED_LOGIN_ID);
        }
    }
}
