package com.example.attractionReservationSystem.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Attraction",
        uniqueConstraints = {@UniqueConstraint(name = "UQ_id", columnNames = {"id"}),
                @UniqueConstraint(name = "UQ_name", columnNames = {"name"})})

public class Attraction {
    // 예상 대기시간 찍어줄 수 있게 속성 추가하면 좋을지도?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;             //고유 id, 기본키

    @Column(name = "name", nullable = false, length = 20)
    String name;        //기구 이름

    @Column(name = "oneCycleTime", nullable = false)
    int oneCycleTime;      // 놀이기구를 한번 타는데 걸리는 시간, 단위는 분

    @Column(name = "oneCyclePeople", nullable = false)
    int oneCyclePeople;      // 놀이기구에 탈 수 있는 사람의 수


    @Column(name = "waitingCount")
    int waitingCount;   //대기 인원
}
