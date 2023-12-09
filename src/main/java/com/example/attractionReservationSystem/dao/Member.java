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
        name = "Member",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_id", columnNames = {"id"}),
                @UniqueConstraint(name = "UQ_memberId", columnNames = {"memberId"})}
        )
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;                     //고유번호, 기본키

    @Column(name = "memberId", nullable = false, length = 20)
    String memberId;            //아이디

    @Column(name = "pw", nullable = false, length = 20)
    String pw;                  //패스워드

    @Column(name = "name", nullable = false, length = 10)
    String name;                //이름

    @Column(name = "reservAttraction")
    String reservAttraction;     //예약한 기구

    @Column(name = "prevAttraction")
    String prevAttraction;     //이전에 예약한 기구

    @Column(name = "waitingNumber")
    int waitingNumber;          //대기번호

    public Member(String memberId, String pw, String name){
        this.memberId = memberId;
        this.pw = pw;
        this.name = name;
    }
}
