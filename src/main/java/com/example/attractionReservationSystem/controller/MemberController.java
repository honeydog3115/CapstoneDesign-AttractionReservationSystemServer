package com.example.attractionReservationSystem.controller;

import com.example.attractionReservationSystem.dao.Member;
import com.example.attractionReservationSystem.dto.Login.LoginRequest;
import com.example.attractionReservationSystem.dto.Login.LoginResponse;
import com.example.attractionReservationSystem.dto.Response;
import com.example.attractionReservationSystem.dto.Signup.SignupRequest;
import com.example.attractionReservationSystem.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberRepository memberRepository;


    // @Valid를 사용해야 추가한 Validation을 통해 검증을 할 수 있음
    @PostMapping("/signup")
    public ResponseEntity<?> registerMember(@Valid @RequestBody SignupRequest signupRequest){
        Response response = new Response("회원가입 성공", true);

        if(signupRequest.getId().contains(" ") || signupRequest.getPw().contains(" ") || signupRequest.getName().contains(" ")){
            response.setMessage("아이디, 비밀번호, 이름에 공백은 포함될 수 없습니다.");
            response.setSuccess(false);

            return ResponseEntity
                    .badRequest()
                    .body(response);
        }

        // 이미 아이디가 존재하는 경우
        if(memberRepository.existsByMemberId(signupRequest.getId())){
            response.setMessage("이미 존재하는 사용자 아이디입니다.");
            response.setSuccess(false);

            return  ResponseEntity
                    .badRequest()
                    .body(response);
        }

        System.out.println(signupRequest.getId());
        System.out.println(signupRequest.getPw());

        Member newMember = new Member();
        newMember.setMemberId(signupRequest.getId());
        newMember.setPw(signupRequest.getPw());
        newMember.setName(signupRequest.getName());
        newMember.setPrevAttraction("");
        newMember.setReservAttraction("");
        newMember.setWaitingNumber(0);

        memberRepository.save(newMember);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@Valid @RequestBody LoginRequest loginRequest){

        System.out.println(loginRequest.getId());
        System.out.println(loginRequest.getPw());

        LoginResponse loginResponse = new LoginResponse("", false, "");

        //일치하는 id가 없는 경우
        if(!memberRepository.existsByMemberId(loginRequest.getId())){
            loginResponse.setMessage("일치하는 사용자 아이디가 없습니다.");
            return ResponseEntity
                    .badRequest()
                    .body(loginResponse);
        }

        if(!memberRepository.existsByMemberIdAndPw(loginRequest.getId(), loginRequest.getPw())){
            loginResponse.setMessage("비밀번호가 틀렸습니다.");
            return ResponseEntity
                    .badRequest()
                    .body(loginResponse);
        }

        loginResponse.setId(loginRequest.getId());
        loginResponse.setLoginSuccess(true);
        loginResponse.setMessage("로그인 성공");

        return ResponseEntity.ok(loginResponse);
    }


}
