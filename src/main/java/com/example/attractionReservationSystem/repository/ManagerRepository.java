package com.example.attractionReservationSystem.repository;

import com.example.attractionReservationSystem.dao.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    // 매니저 Id로 매니저 튜플 가져오기
    Optional<Manager> findByManagerId(String managerId);

    // 로그인시 아이디 pw맞나 검사
    @Query("select 1 from Manager m where m.managerId like :managerId and m.pw like :pw")
    Optional<Integer> checkLogIn(@Param("managerId") String managerId, @Param("pw") String pw);

    // 담당 어트랙션 할당
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Manager m set m.managementAttration = :attractionId where m.managerId like :managerId")
    int setAttraction(@Param("attractionId") String attractionId, @Param("managerId") String managerId);

    // 담당 어트랙션 조회
    @Query("select m.managementAttration from Manager m where m.managerId like :managerId")
    Optional<Integer> findAttractionIdByManagerId(@Param("managerId") String managerId);

    // 매니저 이름 조회
    @Query("select m.name from Manager m where m.managerId like :managerId")
    Optional<String> findNameByManagerId(@Param("managerId") String managerId);

    // 회원가입시 튜플을 저장
    Manager save(Manager manager);

    // 회원 탈퇴
    int deleteByManagerId(String managerId);
}
