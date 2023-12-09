package com.example.attractionReservationSystem.controller;


import com.example.attractionReservationSystem.dao.Attraction;
import com.example.attractionReservationSystem.dao.Manager;
import com.example.attractionReservationSystem.dao.Member;
import com.example.attractionReservationSystem.dao.Reservation;
import com.example.attractionReservationSystem.dto.reservation.ReservTableRespone;
import com.example.attractionReservationSystem.dto.Response;
import com.example.attractionReservationSystem.dto.WaitingResponse;
import com.example.attractionReservationSystem.dto.reservation.ReservAttractionRequest;
import com.example.attractionReservationSystem.dto.reservation.ReservInfoResponse;
import com.example.attractionReservationSystem.repository.AttractionRepository;
import com.example.attractionReservationSystem.repository.ManagerRepository;
import com.example.attractionReservationSystem.repository.MemberRepository;
import com.example.attractionReservationSystem.repository.ReservationRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    AttractionRepository attractionRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ManagerRepository managerRepository;


    ///////////////////////////////////////////////////////////// 사용자 앱에서 호출 /////////////////////////////////////////////////////////////

    // 앱에서 예약을 누르면 실행되는 함수, 대기번호를 반환
    @PostMapping("/reservateAttraction")
    public ResponseEntity<?> reservateAttraction(@Valid @RequestBody ReservAttractionRequest reservAttractionRequest){
         Member member = memberRepository.findByMemberId(reservAttractionRequest.getMemberId())
                 .orElseThrow(() -> new IllegalAccessError("reservateAttraction : 해당하는 멤버가 없습니다."));

        if(member.getReservAttraction().equals(reservAttractionRequest.getAttractionName())){
            return ResponseEntity
                    .badRequest()
                    .body(new Response("해당 어트랙션은 지금 예약된 상태입니다.", false));
        }

        else if(!member.getReservAttraction().equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(new Response("다른 어트랙션을 이미 예약중입니다.", false));
        }

        else if(member.getPrevAttraction().equals(reservAttractionRequest.getAttractionName())){
            return ResponseEntity
                    .badRequest()
                    .body(new Response("이전에 예약했던 어트랙션은 바로 예약할 수 없습니다.", false));
        }

        // 새로운 예약 튜플 생성
        Reservation newReservation = new Reservation();
        newReservation.setMemberId(reservAttractionRequest.getMemberId());

        String attractionName = reservAttractionRequest.getAttractionName();
        System.out.println("+++" + attractionName);
        Attraction reservAttraction = attractionRepository.findAttractionByName(attractionName)
                .orElseThrow(() -> new IllegalAccessError("getReservAttractionId : 해당하는 어트랙션이 없습니다."));

        newReservation.setReservAttraction(reservAttraction.getName());
        newReservation.setWaitingNumber(reservAttraction.getWaitingCount()+1);
        newReservation.setReservCheck(false);


        // 사용자에게 대기번호 부여
        memberRepository.updateWaitingNumber(member.getMemberId(),reservAttraction.getWaitingCount()+1);

        // attraction 테이블의 waitingCount 갱신
        attractionRepository.updateWaitingNumberByName(newReservation.getWaitingNumber(), attractionName);
        memberRepository.setRervAttractionBymemberId(reservAttractionRequest.getAttractionName(), reservAttractionRequest.getMemberId());

        // 예약정보를 테이블에 저장
        reservationRepository.save(newReservation);

        return ResponseEntity.ok(new Response("예약이 완료되었습니다.", true));
    }

    // 별도의 창에서 자신의 예약 정보를 볼때
    @GetMapping("/info/{memberId}")
    public ResponseEntity<?> getReservationInfo(@PathVariable String memberId){
        Member reservedMember = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalAccessError("getReservationInfo : 해당하는 멤버가 없습니다."));

        String attraction = reservedMember.getReservAttraction();

        if(attraction.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(new ReservInfoResponse(0,-1));
        }

        Attraction reservAttraction = attractionRepository.findAttractionByName(attraction)
                .orElseThrow(()->new IllegalAccessError("getReservAttractionId : 해당하는 어트랙션이 없습니다."));

        int oneCycleTime = reservAttraction.getOneCycleTime();
        int oneCyclePeople = reservAttraction.getOneCyclePeople();
        int memberWaitingNumber = reservedMember.getWaitingNumber()-1;

        int predictWaitingTime = (memberWaitingNumber/oneCyclePeople)*oneCycleTime;

        return ResponseEntity.ok(new ReservInfoResponse(memberWaitingNumber, predictWaitingTime));
    }

    // 지도에서 예약 정보 보여주기
    @GetMapping("/waitingInfo/{attractionName}/{memberId}")
    public ResponseEntity<?> getwaitingInfo(@PathVariable String attractionName, @PathVariable String memberId){
        Attraction attraction = attractionRepository.findAttractionByName(attractionName)
                .orElseThrow(()->new IllegalAccessError("getReservAttractionId : 해당하는 어트랙션이 없습니다."));

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalAccessError("reservateAttraction : 해당하는 멤버가 없습니다."));

        System.out.println("버튼 어트랙션 이름 : " + attractionName);


        String reservAttraction = member.getReservAttraction();
        System.out.println("예약한 어트랙션 이름 : " + reservAttraction);
        System.out.println("사용자 웨이팅 넘버 : " + member.getWaitingNumber());
        int oneCycleTime;
        int oneCyclePeople;
        int waitingPeopleCount;
        int predictWaitingTime;
        int memberWaitingNumber;

        WaitingResponse waitingResponse = new WaitingResponse(-1, -1);

        if(attractionName.equals(reservAttraction)) {
            System.out.println("if 계산식 진입");
            oneCycleTime = attraction.getOneCycleTime();
            oneCyclePeople = attraction.getOneCyclePeople();
            memberWaitingNumber = member.getWaitingNumber() - 1;
            System.out.println("사용자 대기 번호 : " + memberWaitingNumber);
            predictWaitingTime = (memberWaitingNumber / oneCyclePeople) * oneCycleTime;

            waitingResponse.setWaitingNumber(memberWaitingNumber);
            waitingResponse.setWaitingTime(predictWaitingTime);
        }
        else{
            System.out.println("else 계산식 진입");
            oneCycleTime = attraction.getOneCycleTime();
            oneCyclePeople = attraction.getOneCyclePeople();
            waitingPeopleCount = attraction.getWaitingCount();
            predictWaitingTime = (waitingPeopleCount/oneCyclePeople)*oneCycleTime;

            waitingResponse.setWaitingNumber(waitingPeopleCount);
            waitingResponse.setWaitingTime(predictWaitingTime);
        }


        return ResponseEntity.ok(waitingResponse);
    }

    @GetMapping("/table/{memberId}")
    public ResponseEntity<?> getAllwaitingInfo(@PathVariable String memberId){
        List<Attraction> attractions = attractionRepository.findAll();
        List<ReservTableRespone> reservTableRespone = new ArrayList<>();

        System.out.println("+++" + memberId);

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalAccessError("reservateAttraction : 해당하는 멤버가 없습니다."));

        String reservAttraction = member.getReservAttraction();
        int oneCycleTime;
        int oneCyclePeople;
        int waitingPeopleCount;
        int predictWaitingTime;
        int memberWaitingNumber;

        for(Attraction attraction: attractions) {
            boolean reservCheck = false;
            if(member.getReservAttraction().equals(attraction.getName()))
                reservCheck = true;

            if(!attraction.equals(reservAttraction)){
                oneCycleTime = attraction.getOneCycleTime();
                oneCyclePeople = attraction.getOneCyclePeople();
                waitingPeopleCount = attraction.getWaitingCount();
                predictWaitingTime = (waitingPeopleCount/oneCyclePeople)*oneCycleTime;
                reservTableRespone.add(new ReservTableRespone(waitingPeopleCount, predictWaitingTime, reservCheck));
            }
            else{
                oneCycleTime = attraction.getOneCycleTime();
                oneCyclePeople = attraction.getOneCyclePeople();
                memberWaitingNumber = member.getWaitingNumber()-1;
                predictWaitingTime = (memberWaitingNumber/oneCyclePeople)*oneCycleTime;
                reservTableRespone.add(new ReservTableRespone(memberWaitingNumber, predictWaitingTime, reservCheck));
            }
        }

        return ResponseEntity.ok(reservTableRespone.toArray());
    }

    @GetMapping("/delete/{memberId}")
    public ResponseEntity<?> deleteReservation(@PathVariable String memberId){
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalAccessError("reservateAttraction : 해당하는 멤버가 없습니다."));

        if(member.getReservAttraction().equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(new Response("예약하신 어트랙션이 없습니다.", false));
        }

        reservationRepository.deleteByMemberId(memberId);
        memberRepository.setRervAttractionBymemberId("", memberId);

        return ResponseEntity.ok(new Response("예약이 취소되었습니다.",true));
    }

    ///////////////////////////////////////////////////////////// 관리자 앱에서 호출 /////////////////////////////////////////////////////////////

    // QR체크시, ReservCheck를 true로 바꾸고 대기번호를 줄이는 함수
    // memberId는 QR을 통해 받아온 값으로 처리하고, attractionId는 사용자한테 받을지, 관리자가 관리하는 Id에서 받을지 선택
    @GetMapping("/check/{memberId}/{managerId}")
    public ResponseEntity<?> getReservationCheck(@PathVariable String memberId, @PathVariable String managerId){
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalAccessError("getReservationCheck : 해당하는 멤버가 없습니다."));

        Manager manager = managerRepository.findByManagerId(managerId)
                .orElseThrow(() -> new IllegalAccessError("getReservationCheck : 해당하는 매니저가 없습니다."));


        if(!manager.getManagementAttration().equals(member.getReservAttraction())){
            System.out.println("어트랙션 불일치");
            return ResponseEntity
                    .badRequest()
                    .body(new Response("예약한 어트랙션과 일치하지 않습니다.", false));
        }

        // 이거 지우기, 멤버의 웨이팅 넘버를 1로 조정
        //reservationRepository.updateWaitingNumberByMemberId(memberId);

        if(reservationRepository.findWaitingNumberBymemberId(memberId) > 20){
            System.out.println("순번이 아직임" + reservationRepository.findWaitingNumberBymemberId(memberId));
            return ResponseEntity
                    .badRequest()
                    .body(new Response("아직 순번이 아닙니다.", false));
        }

        // 예약 여부를 true로 설정
        reservationRepository.updateReservCheck(memberId, manager.getManagementAttration());
        // 예약 테이블의 모든 웨이팅 넘버 조정
        reservationRepository.updateWaitingNumber(member.getWaitingNumber(), manager.getManagementAttration());
        // 멤버의 웨이팅 넘버 조정
        memberRepository.updateWaitingNumber(memberId, reservationRepository.findWaitingNumberBymemberId(memberId));
        // 멤버 테이블의 prevAttraction을 업데이트
        memberRepository.updatePrevAttraction(memberId, manager.getManagementAttration());
        // 멤버 테이블의 reservAttraction을 업데이트
        memberRepository.setRervAttractionBymemberId("", memberId);
        // 어트랙션의 웨이팅 넘버를 조정
        attractionRepository.updateWaitingNumberByName( reservationRepository.findMaxWaitingNumberByAttraction(member.getReservAttraction()),member.getReservAttraction());
        // 예약 테이블에서 예약 정보를 삭제
        reservationRepository.deleteByMemberId(memberId);

        return ResponseEntity.ok(new Response("통과입니다.", true));
    }
}
