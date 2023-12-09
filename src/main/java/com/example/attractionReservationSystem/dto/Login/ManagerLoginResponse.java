package com.example.attractionReservationSystem.dto.Login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ManagerLoginResponse {
    // 아이디
    String id;

    // 로그인 성공 여부
    boolean loginSuccess;

    // 로그인 성공 메시지
    String message;

    String attraction;

    String name;
}
