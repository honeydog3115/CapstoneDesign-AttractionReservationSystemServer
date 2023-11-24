package com.example.attractionReservationSystem.controller;

import com.example.attractionReservationSystem.dao.Member;
import com.example.attractionReservationSystem.dto.LoginRequest;
import com.example.attractionReservationSystem.dto.SignupRequest;
import com.example.attractionReservationSystem.repository.MemberRepository;
import com.example.attractionReservationSystem.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @GetMapping()
    public ResponseEntity<?> getMemberList() {
        System.out.println("api 호출");

        return ResponseEntity.ok(memberService.getMemberList());
    }

    // @Valid를 사용해야 추가한 Validation을 통해 검증을 할 수 있음
    @PostMapping("/signup")
    public ResponseEntity<?> registerMember(@Valid @RequestBody SignupRequest signupRequest){
        // 이미 아이디가 존재하는 경우
        if(memberRepository.existsByMemberId(signupRequest.getMemberId())){
            return  ResponseEntity
                    .badRequest()
                    .body(new String("Error : 이미 존재하는 id입니다."));
        }
        Member newMember = new Member();
        newMember.setMemberId(signupRequest.getMemberId());
        newMember.setPw(signupRequest.getPw());
        newMember.setName(signupRequest.getName());

        memberRepository.save(newMember);

        return ResponseEntity.ok(new String("회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@Valid @RequestBody LoginRequest loginRequest){
        // 일치하는 id가 없는 경우
        if(memberRepository.existsByMemberId(loginRequest.getMemberId())){
            return ResponseEntity
                    .badRequest()
                    .body(new String("Error : 일치하는 id가 없습니다."));
        }
        if(memberRepository.existsByMemberIdAndAndPw(loginRequest.getMemberId(), loginRequest.getPw())){
            return ResponseEntity
                    .badRequest()
                    .body(new String("Error : pw가 틀렸습니다."));
        }

        return ResponseEntity.ok(new String("로그인 성공"));
    }


}
