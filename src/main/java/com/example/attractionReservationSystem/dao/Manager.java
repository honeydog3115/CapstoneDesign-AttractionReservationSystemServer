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
@Table(
        name = "Manager",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_id", columnNames = {"id"}),
                @UniqueConstraint(name = "UQ_managerId", columnNames = {"managerId"})}
)
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 숫자를 1씩 증가시켜줌
    int id;                     // 고유 id, 기본키

    @Column(name = "managerId", nullable = false, length = 20)
    String managerId;           // 매니저 아이디

    @Column(name = "pw", nullable = false, length = 20)
    String pw;                  // 매니저 비밀번호

    @Column(name = "name", nullable = false, length = 10)
    String name;                // 매니저 이름

    @Column(name = "managementAttraction", length = 20)
    String managementAttration; // 담당 놀이기구

}
