package squad.board.repository;

import org.apache.ibatis.annotations.Mapper;
import squad.board.domain.member.Member;
import squad.board.dto.member.MemberUpdateRequest;

@Mapper
public interface MemberMapper {
    // 회원 저장
    void save(Member member);

    // PK로 회원 조회
    Member findById(Long memberId);

    // LoginId와 Password로 회원 조회
    Member findMemberByLoginIdAndLoginPw(String loginId, String loginPw);

    // LoginId 로 회원 조회
    Member findByLoginIdOrNickName(String memberInfo);

    // 회원 정보 업데이터
    void update(Long memberId, MemberUpdateRequest memberUpdateRequest);
}
