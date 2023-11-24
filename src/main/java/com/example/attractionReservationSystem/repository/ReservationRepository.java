package com.example.attractionReservationSystem.repository;

import com.example.attractionReservationSystem.dao.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 사용자 Id로 튜플 가져오기
    Optional<Reservation> findByMemberId(String memberId);


    // 담당자 예약 확인 설정
    // 업데이트 문은 영향받은 행의(튜플)의 수를 반환함.
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Reservation r set r.reservCheck = true where r.memberId like :memberId and r.reservAttractionId = :attractionId")
    int updateReservCheck(@Param("memberId") String memberId, @Param("attractionId") int attractoinId);

    // 대기번호 조정
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Reservation r " +
            "set r.waitingNumber = (:waitingnumber -1) " +
            "where r.reservAttractionId = :attractionId and r.waitingNumber <= :waitingnumber")
    int updateWaitingNumber(@Param("waitingnumber") int waitingnumber, @Param("attractionId") int attractionId);

    // 대기번호 조회
    @Query("select r.waitingNumber from Reservation r where r.memberId = :memberId")
    int findwaitingNumberBymemberId(@Param("memberId") String memberId);

    // 가장 높은 대기번호 조회, 예약시 대기 번호 부여를 위함.
    @Query("select max(r.waitingNumber) from Reservation r where r.id = :attractionId")
    int findMaxWaitingNumberByAttractionId(@Param("attractionId") int attractionId);

    Reservation save(Reservation reservation);

    // 영향을 받은 행의 수를 반환.
    int deleteByMemberId(String memberId);
}
