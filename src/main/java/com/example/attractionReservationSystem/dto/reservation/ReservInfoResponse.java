package com.example.attractionReservationSystem.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReservInfoResponse {
    int waitingNumber;

    int waitingTime;
}
