package com.example.attractionReservationSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank
    @Size(min = 5, max = 20)
    String memberId;

    @NotBlank
    @Size(min = 5, max = 20)
    String pw;

}
