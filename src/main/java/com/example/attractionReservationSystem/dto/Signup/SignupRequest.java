package com.example.attractionReservationSystem.dto.Signup;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    // @NotBlank : null, "", " " 을 허용하지 않는 Validation을 추가
    // @Size : 크기와 관련된 Validation을 추가
    @NotBlank
    @Size(min = 5, max = 20)
    String id;

    @NotBlank
    @Size(min = 5, max = 20)
    String pw;

    @NotBlank
    String name;
}
