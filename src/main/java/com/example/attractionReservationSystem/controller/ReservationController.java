package com.example.attractionReservationSystem.controller;


import com.example.attractionReservationSystem.dao.Member;
import com.example.attractionReservationSystem.dao.Reservation;
import com.example.attractionReservationSystem.dto.ReservAttractionRequest;
import com.example.attractionReservationSystem.repository.MemberRepository;
import com.example.attractionReservationSystem.repository.ReservationRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    MemberRepository memberRepository;


    ///////////////////////////////////////////////////////////// 사용자 앱에서 호출 /////////////////////////////////////////////////////////////

    // 앱에서 예약을 누르면 실행되는 함수, 대기번호를 반환
    @PostMapping("/reservateAttraction")
    public ResponseEntity<?> reservateAttraction(@Valid @RequestBody ReservAttractionRequest reservAttractionRequest){
        Reservation newReservation = new Reservation();
        newReservation.setMemberId(reservAttractionRequest.getMemberId());
        newReservation.setReservAttractionId(reservAttractionRequest.getReservAttractionId());
        // 대기번호 부여
        newReservation.setWaitingNumber(reservationRepository
                .findMaxWaitingNumberByAttractionId(newReservation.getReservAttractionId())+1);
        newReservation.setReservCheck(false);

        reservationRepository.save(newReservation);

        return ResponseEntity.ok(newReservation.getWaitingNumber());
    }

    // 사용자가 예약정보를 보려할 때 호출할 함수, 어트백션 id와 대기번호 반환
    @GetMapping("/reservationInfo/{memberId}")
    public ResponseEntity<?> getReservationInfo(@PathVariable String memberId){
        Member reservedMember = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalAccessError("getReservationInfo : 해당하는 멤버가 없습니다."));

        int attractionId = reservedMember.getReservAttractionId();
        int waitingNumber = reservedMember.getWaitingNumber();

        return ResponseEntity.ok(new int[]{attractionId, waitingNumber});
    }

    // 사용자가 어트랙션을 예약하기 전에 현재 예약한 사람들의 수 보여주기
    @GetMapping("/waitingInfo/{attractionId}")
    public ResponseEntity<?> getwaitingInfo(@PathVariable int attractionId){
        int maxWaitingNumber = reservationRepository.findMaxWaitingNumberByAttractionId(attractionId);
        return ResponseEntity.ok(maxWaitingNumber);
    }

    ///////////////////////////////////////////////////////////// 관리자 앱에서 호출 /////////////////////////////////////////////////////////////

    // QR체크시, ReservCheck를 true로 바꾸고 대기번호를 줄이는 함수
    // memberId는 QR을 통해 받아온 값으로 처리하고, attractionId는 사용자한테 받을지, 관리자가 관리하는 Id에서 받을지 선택
    @GetMapping("/check/{memberId}/{attractionId}")
    public ResponseEntity<?> getReservationCheck(@PathVariable String memberId, @PathVariable int attractionId){
        Member reservedMember = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalAccessError("getReservationCheck : 해당하는 멤버가 없습니다."));

        reservationRepository.updateReservCheck(memberId, attractionId);
        reservationRepository.updateWaitingNumber(reservedMember.getWaitingNumber(), attractionId);
        memberRepository.updatePrevAttractionId(memberId, attractionId);
        reservationRepository.deleteByMemberId(memberId);

        return ResponseEntity.ok(false);
    }


}
