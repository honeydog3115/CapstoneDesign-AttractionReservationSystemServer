package com.example.attractionReservationSystem.repository;

import com.example.attractionReservationSystem.dao.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    @Override
    List<Attraction> findAll();

    // 어트랙션 이름으로 정보 가져오기
    Optional<Attraction> findAllByName(String name);

    Optional<Attraction> findAttractionByName(String name);

    // 이름에 해당하는 어트랙션 대기 수 가져오기
    @Query("select a.waitingCount from Attraction a where a.name like :name")
    Optional<Integer> findWaitingCountByName(@Param("name") String name);


    boolean existsByName(String name);

    // id와 일치하는 어트랙션 정보 가져오기
    @Query("select a from Attraction a where a.id = :id")
    Optional<Attraction> findAllAttractionById(int id);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Attraction a set a.waitingCount = :newWaitingNumber where a.name like :attractionName")
    int updateWaitingNumberByName(@Param("newWaitingNumber") int newWaitingNumber, @Param("attractionName") String attractionName);

    // 어트랙션 개수세기
    @Query("select count(a) from Attraction a")
    Optional<Long> countAttractions();

}
