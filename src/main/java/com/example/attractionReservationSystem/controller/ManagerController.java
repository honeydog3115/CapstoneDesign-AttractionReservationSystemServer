package com.example.attractionReservationSystem.controller;

import com.example.attractionReservationSystem.dao.Manager;
import com.example.attractionReservationSystem.dto.Login.LoginRequest;
import com.example.attractionReservationSystem.dto.Login.ManagerLoginResponse;
import com.example.attractionReservationSystem.dto.Response;
import com.example.attractionReservationSystem.dto.Signup.ManagerSignupRequest;
import com.example.attractionReservationSystem.dto.Signup.SignupRequest;
import com.example.attractionReservationSystem.repository.AttractionRepository;
import com.example.attractionReservationSystem.repository.ManagerRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    ManagerRepository managerRepository;
    AttractionRepository attractionRepository;

    @GetMapping()
    public ResponseEntity<?> getManagerList() {
        return ResponseEntity.ok(managerRepository.findAll());
    }
    //가입을 위한 함수
    @PostMapping("/signup")
    public ResponseEntity<?> registerManager(@Valid @RequestBody ManagerSignupRequest signupRequest) {
        Response response = new Response("회원가입 성공", true);

        if(signupRequest.getId().contains(" ") || signupRequest.getPw().contains(" ") || signupRequest.getName().contains(" ")){
            response.setMessage("아이디, 비밀번호, 이름에 공백은 포함될 수 없습니다.");
            response.setSuccess(false);

            return ResponseEntity
                    .badRequest()
                    .body(response);
        }

        if(managerRepository.existsByManagerId(signupRequest.getId())) {
            response.setMessage("이미 존재하는 매니저 아이디입니다.");
            response.setSuccess(false);

            return ResponseEntity
                    .badRequest()
                    .body(response);
        }

        if(signupRequest.getAttraction().equals("")){
            response.setMessage("담당 어트랙션을 입력해 주세요.");
            response.setSuccess(false);

            return ResponseEntity
                    .badRequest()
                    .body(response);
        }
        else {
            if (!attractionRepository.existsByName(signupRequest.getAttraction())) {
                response.setMessage("해당 어트랙션은 존재하지 않습니다.");
                response.setSuccess(false);

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }
        }

        Manager newManager = new Manager();
        newManager.setManagerId(signupRequest.getId());
        newManager.setPw(signupRequest.getPw());
        newManager.setName(signupRequest.getName());
        newManager.setManagementAttration(signupRequest.getAttraction());

        managerRepository.save(newManager);

        return ResponseEntity.ok(response);
    }
    ///로그인을 위한 함수
    @PostMapping("/login")
    public ResponseEntity<?> loginManager(@Valid @RequestBody LoginRequest loginRequest) {
        ManagerLoginResponse managerLoginResponse =
                new ManagerLoginResponse("", false, "", "", "");

        if (!managerRepository.existsByManagerId(loginRequest.getId())) {
            managerLoginResponse.setMessage("일치하는 매니저 아이디가 없습니다.");

            return ResponseEntity
                    .badRequest()
                    .body(managerLoginResponse);
        }

        if (!managerRepository.existsByManagerIdAndAndPw(loginRequest.getId(), loginRequest.getPw())) {
            managerLoginResponse.setMessage("비밀번호가 틀렸습니다.");

            return ResponseEntity
                    .badRequest()
                    .body(managerLoginResponse);
        }

        Manager manager = managerRepository.findByManagerId(loginRequest.getId()).get();

        managerLoginResponse.setId(loginRequest.getId());
        managerLoginResponse.setLoginSuccess(true);
        managerLoginResponse.setMessage("로그인 성공");
        managerLoginResponse.setAttraction(manager.getManagementAttration());
        managerLoginResponse.setName(manager.getName());

        return ResponseEntity.ok(managerLoginResponse);
    }
    /////담당 놀이기구 설정을 위한 함수 매니저 아이디와 놀이기구 이름을 받는다.
    @PostMapping("/setAttraction")
    public ResponseEntity<?> setAttractionForManager(@RequestParam String managerId, @RequestParam String attractionName) {
        if (!managerRepository.existsByManagerId(managerId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new String("일치하는 매니저 아이디가 없습니다."));
        }

        int result = managerRepository.setAttraction(attractionName, managerId);

        if (result == 1) {
            return ResponseEntity.ok(new String("담당 놀이기구 설정이 완료되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(new String("담당 놀이기구 설정에 실패했습니다."));
        }
    }
}
