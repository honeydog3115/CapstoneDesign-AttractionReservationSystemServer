package com.example.attractionReservationSystem.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReservAttractionRequest {
    @NotBlank
    @Size(min = 5, max = 20)
    String memberId;

    String attractionName ;

}
