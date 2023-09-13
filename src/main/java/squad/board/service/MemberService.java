package squad.board.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.member.Member;
import squad.board.dto.member.CreateMemberRequest;
import squad.board.dto.member.LoginRequest;
import squad.board.exception.login.LoginException;
import squad.board.exception.session.SessionException;
import squad.board.repository.MemberMapper;

import static squad.board.exception.login.LoginStatus.DUPLICATED_LOGIN_ID;
import static squad.board.exception.login.LoginStatus.INVALID_LOGIN_INFO;
import static squad.board.exception.session.SessionStatus.INVALID_SESSION_ID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberMapper memberMapper;

    @Transactional
    public CommonIdResponse join(CreateMemberRequest createMember) {
        // 서버 쪽에서 한 번 더 중복아이디 검증처리
        validationLoginId(createMember.getLoginId());
        Member member = createMember.toEntity();
        memberMapper.save(member);
        return new CommonIdResponse(member.getMemberId());
    }

    // Id로 회원 조회
    public Member findMember(Long memberId) {
        return memberMapper.findById(memberId);
    }

    // 회원 로그인
    public Member login(LoginRequest loginRequest) {
        String loginId = loginRequest.getLoginId();
        String loginPw = loginRequest.getLoginPw();
        log.info("{} Login", loginId);
        return memberMapper.findMemberByLoginIdAndLoginPw(loginId, loginPw);
    }

    public CommonIdResponse logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        log.info("{} Logout", memberId);
        session.invalidate();
        return new CommonIdResponse(memberId);
    }

    // 세션 발급
    public CommonIdResponse provideSession(Member member, HttpServletRequest request) {
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

        return new CommonIdResponse(member.getMemberId());
    }

    // 세션 검증
    public Long validateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // (주소창으로 직접)세션이 없는 접근이거나, 세션이 만료되면 메인으로 리다이렉션
        if (session == null) {
            throw new SessionException(INVALID_SESSION_ID);
        }
        return (Long) session.getAttribute("memberId");
    }

    // 회원가입 시 중복아이디 검증
    public void validationLoginId(String loginId) {
        Member findMember = memberMapper.findByLoginId(loginId);
        if (findMember != null) {
            throw new LoginException(DUPLICATED_LOGIN_ID);
        }
    }
}
