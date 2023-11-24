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
        name = "Reservation",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_id", columnNames = {"id"})}
)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;                 // 고유 번호, 기본키

    @Column(name = "memberId", nullable = false, length = 20)
    String memberId;

    @Column(name = "reservAttractionId", nullable = false)
    int reservAttractionId; // 예약한 놀이기구 ID

    @Column(name = "waitingNumber", nullable = false)
    int waitingNumber;      // 대기번호

    @Column(name = "reservCheck", nullable = false)
    boolean reservCheck;    // 담당자 예약 확인 여부

    public Reservation(String memberId, int reservAttractionId, int waitingNumber, boolean reservCheck){
        this.memberId = memberId;
        this.reservAttractionId = reservAttractionId;
        this.waitingNumber = waitingNumber;
        this.reservCheck = reservCheck;
    }
}
