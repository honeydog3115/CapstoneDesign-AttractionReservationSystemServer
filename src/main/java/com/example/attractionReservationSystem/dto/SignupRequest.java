package com.example.attractionReservationSystem.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    // @NotBlank : null, "", " " 을 허용하지 않는 Validation을 추가
    // @Size : 크기와 관련된 Validation을 추가
    @NotBlank
    @Size(min = 5, max = 20)
    String memberId;

    @NotBlank
    @Size(min = 5, max = 20)
    String pw;
    @NotBlank
    String name;
}
