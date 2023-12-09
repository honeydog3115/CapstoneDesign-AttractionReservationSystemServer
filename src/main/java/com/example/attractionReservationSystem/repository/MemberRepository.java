package com.example.attractionReservationSystem.repository;

import com.example.attractionReservationSystem.dao.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Query("select m.reservAttraction from Member m where m.memberId = :memberId")
    Optional<Integer> findReservAttractionIdByMeberId(@Param("memberId") String memberId);

    // memberId, pw와 일치하는 멤버가 있는지 확인
    //boolean existsByMemberIdAndPw(String memberId, String pw);

    boolean existsByMemberIdAndPw(String memberId, String pw);

    // MemberId로 대기번호 찾기
    @Query("select m.waitingNumber from Member m where m.memberId = :memberId")
    int findWaitingNumberIdByMeberId(@Param("memberId") String memberId);

    // MemberId로 이전 어트랙션 Id 찾기
    @Query("select m.prevAttraction from Member m where m.memberId = :memberId")
    Optional<String> findPrevAttractionIdByMemberId(@Param("memberId") String memberId);

    // prevAttractionId 에 값 할당하기
    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Member m set m.prevAttraction = :attraction where m.memberId like :memberId")
    int updatePrevAttraction(@Param("memberId") String memberId, @Param("attraction") String attraction);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Member m set m.waitingNumber = :newWaitingNumber where m.memberId like :memberId")
    int updateWaitingNumber(@Param("memberId") String memberId, @Param("newWaitingNumber") int newWaitingNumber);

    // MemberId에 해당하는 예약한 어트랙션 Id 바꾸기
    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Member m set m.reservAttraction = :attraction where m.memberId = :memberId")
    int setRervAttractionBymemberId(@Param("attraction") String reservAttraction, @Param("memberId") String memberId);

    // Member 삽입하기
    Member save(Member member);

}
