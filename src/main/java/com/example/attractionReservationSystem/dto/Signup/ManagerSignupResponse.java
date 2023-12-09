package com.example.attractionReservationSystem.dto.Signup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ManagerSignupResponse {
    String message;
    boolean success;
    String attraction;
}
