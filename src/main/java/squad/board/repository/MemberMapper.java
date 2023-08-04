package squad.board.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;
import squad.board.domain.member.Member;

@Mapper
public interface MemberMapper {
    // 회원 저장
    void save(Member member);

    // PK로 회원 조회
    Member findById(Long memberId);

    // 로그인
    Member findMemberByLoginIdAndLoginPw(String loginId, String loginPw);

    // 중복아이디 검증
    int findByLoginId(String loginId);
}
