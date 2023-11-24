package com.example.attractionReservationSystem.repository;

import com.example.attractionReservationSystem.dao.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
    // 모든 멤버 가져오기
    List<Member> findAll();

    // MemberId로 멤버 찾기
    boolean existsByMemberId(String memberId);
    Optional<Member> findByMemberId(String memberId);

    // MemberId로 예약한 어트랙션 Id 찾기
    @Query("select m.reservAttractionId from Member m where m.memberId = :memberId")
    Optional<Integer> findReservAttractionIdByMeberId(@Param("memberId") String memberId);

    // memberId, pw와 일치하는 멤버가 있는지 확인
    boolean existsByMemberIdAndAndPw(String memberId, String pw);

    // MemberId로 대기번호 찾기
    @Query("select m.waitingNumber from Member m where m.memberId = :memberId")
    Optional<Integer> findWaitingNumberIdByMeberId(@Param("memberId") String memberId);

    // MemberId로 이전 어트랙션 Id 찾기
    @Query("select m.prevAttractionId from Member m where m.memberId = :memberId")
    Optional<Integer> findPrevAttractionIdByMemberId(@Param("memberId") String memberId);

    // prevAttractionId 에 값 할당하기
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Member m set m.prevAttractionId = :attractionId where m.memberId like :memberId")
    int updatePrevAttractionId(@Param("memberId") String memberId, @Param("attractionId") int attractionId);

    // MemberId에 해당하는 예약한 어트랙션 Id 바꾸기
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Member m set m.reservAttractionId = :attractionId where m.memberId = :memberId")
    int setRervAttractionIdBymemberId(@Param("attractionId") int rervAttractionId, @Param("memberId") String memberId);

    // Member 삽입하기
    Member save(Member member);

}
