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
import squad.board.exception.login.MemberException;
import squad.board.exception.session.SessionException;
import squad.board.repository.MemberMapper;
import squad.board.validation.MemberInfo;

import java.util.Optional;

import static squad.board.exception.login.MemberStatus.*;
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
        createMember.duplicationChk(memberMapper);
        Member member = createMember.toEntity();
        memberMapper.save(member);
        return new CommonIdResponse(member.getMemberId());
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        // 서버에서 한 번 더 검증
        memberUpdateRequest.duplicationChk(memberMapper, memberId);
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
    public Optional<Member> login(LoginRequest loginRequest) {
        String loginId = loginRequest.getLoginId();
        String loginPw = loginRequest.getLoginPw();
        return Optional.ofNullable(memberMapper.findMemberByLoginIdAndLoginPw(loginId, loginPw));
    }

    public CommonIdResponse logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        log.info("{} Logout", memberId);
        session.invalidate();
        return new CommonIdResponse(memberId);
    }

    // 세션 발급
    public CommonIdResponse provideSession(Optional<Member> member, HttpServletRequest request) {
        // 로그인 정보 검증
        Member loginMember = member.orElseThrow(() -> new MemberException(INVALID_LOGIN_INFO));
        // 기존 세션은 파기
        request.getSession().invalidate();
        // 세션이 없다면 새로운 세션 생성
        HttpSession session = request.getSession(true);
        // 세션에 member의 PK 값을 Value로 세팅
        session.setAttribute("memberId", loginMember.getMemberId());
        // 유효 시간은 30분
        session.setMaxInactiveInterval(1800);

        log.info("{} Login", loginMember.getMemberId());

        return new CommonIdResponse(loginMember.getMemberId());
    }

    // 세션 검증
    public void validateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // (주소창으로 직접)세션이 없는 접근이거나, 세션이 만료되면 메인으로 리다이렉션
        if (session == null) {
            throw new SessionException(INVALID_SESSION_ID);
        }
    }

    // 중복 검증
    @Transactional(readOnly = true)
    public void validationMemberInfo(MemberInfo memberInfo) {
        memberInfo.duplicationChk(memberMapper);
    }
}
