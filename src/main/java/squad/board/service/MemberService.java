package squad.board.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squad.board.commonresponse.CommonIdResponse;
import squad.board.domain.member.Member;
import squad.board.dto.member.*;
import squad.board.exception.login.LoginException;
import squad.board.exception.session.SessionException;
import squad.board.repository.MemberMapper;

import static squad.board.exception.login.LoginStatus.*;
import static squad.board.exception.session.SessionStatus.INVALID_SESSION_ID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberMapper memberMapper;

    @Transactional
    public CommonIdResponse join(CreateMemberRequest createMember) {
        // 서버 쪽에서 한 번 더 중복아이디 검증처리
        validationLoginId(createMember.getLoginId(), null);
        Member member = createMember.toEntity();
        memberMapper.save(member);
        return new CommonIdResponse(member.getMemberId());
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        validationLoginId(memberUpdateRequest.getLoginId(), memberId);
        validationNickName(memberUpdateRequest.getNickName(), memberId);
        memberMapper.update(memberId, memberUpdateRequest);
    }

    // Id로 회원 조회
    @Transactional(readOnly = true)
    public MemberResponse findMember(Long memberId) {
        Member member = memberMapper.findById(memberId);
        return new MemberResponse(member);
    }

    // 회원 로그인
    @Transactional(readOnly = true)
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
    public void validateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // (주소창으로 직접)세션이 없는 접근이거나, 세션이 만료되면 메인으로 리다이렉션
        if (session == null) {
            throw new SessionException(INVALID_SESSION_ID);
        }
    }

    // 회원가입 시 중복아이디 검증
    @Transactional(readOnly = true)
    public void validationLoginId(String loginId, Long memberId) {
        Member findMember = memberMapper.findByLoginId(loginId);
        if (findMember != null && !findMember.getMemberId().equals(memberId)) {
            throw new LoginException(DUPLICATED_LOGIN_ID);
        }
    }

    // 회원가입 시 중복닉네임 검증
    @Transactional(readOnly = true)
    public void validationNickName(String nickName, Long memberId) {
        Member findMember = memberMapper.findByNickName(nickName);
        if (findMember != null && !findMember.getMemberId().equals(memberId)) {
            throw new LoginException(DUPLICATED_NICK_NAME);
        }
    }
}
