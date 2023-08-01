package squad.board.repository;

import org.apache.ibatis.annotations.Mapper;
import squad.board.domain.member.Member;

@Mapper
public interface MemberMapper {
    // 회원 저장
    void save(Member member);

    // PK로 회원 조회
    Member findById(Long memberId);

    Member findMemberByLoginIdAndLoginPw(String loginId, String loginPw);
}
