package com.example.attractionReservationSystem.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReservTableRespone {
    int waitingNumber;
    int waitingTime;
    boolean reservCheck;
}
