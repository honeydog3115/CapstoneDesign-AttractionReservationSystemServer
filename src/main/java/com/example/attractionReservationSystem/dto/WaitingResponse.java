package com.example.attractionReservationSystem.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class WaitingResponse {
    int waitingNumber;

    int waitingTime;
}
