package com.example.attractionReservationSystem.controller;

import com.example.attractionReservationSystem.dto.Login.LoginResponse;
import com.example.attractionReservationSystem.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.function.ServerResponse;

@ControllerAdvice   // 전역 설정을 위한 annotaion
@RestController
public class ControllerExeptionAdviser {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> processValidationError(MethodArgumentNotValidException exception , HandlerMethod handlerMethod) {
        BindingResult bindingResult = exception.getBindingResult();

        String methodName = handlerMethod.getMethod().getName();

        StringBuilder builder = new StringBuilder();

        String errorMessage = "";
        String idMessage = "";
        String pwMessage = "";
        String nameMessage = "";

        Response response = new Response("", false);
        LoginResponse loginResponse = new LoginResponse(null, false, "");

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if(fieldError.getField().toString().equals("id")){
                if(!builder.isEmpty())
                    builder.delete(0,builder.length());
                builder.append(fieldError.getField());
                builder.append("는 ");
                builder.append(fieldError.getDefaultMessage());
                idMessage = builder.toString();
            }
            if(fieldError.getField().toString().equals("pw")){
                if(!builder.isEmpty())
                    builder.delete(0,builder.length());
                builder.append(fieldError.getField());
                builder.append("는 ");
                builder.append(fieldError.getDefaultMessage());
                pwMessage = builder.toString();
            }
            if(fieldError.getField().toString().equals("name")){
                if(!builder.isEmpty())
                    builder.delete(0,builder.length());
                builder.append(fieldError.getField());
                builder.append("은 ");
                builder.append(fieldError.getDefaultMessage());
                nameMessage = builder.toString();
            }
            // 입력된 값이 무엇인지 알려주는 함수
//          builder.append(fieldError.getRejectedValue());
        }

        idMessage = idMessage.replace("id", "아이디");
        pwMessage = pwMessage.replace("pw", "비밀번호");
        nameMessage = nameMessage.replace("name", "이름");
        errorMessage = idMessage + "\n" + pwMessage + "\n" + nameMessage;

        if(methodName.contains("login")){
            loginResponse.setMessage(errorMessage);
            loginResponse.setLoginSuccess(false);
        }
        else{
            response.setMessage(errorMessage);
            response.setSuccess(false);
        }

        System.out.println(methodName);
        System.out.println(idMessage);
        System.out.println(pwMessage);
        System.out.println(nameMessage);

        Object returnObject;
        if(!loginResponse.getMessage().equals("")){
            returnObject = loginResponse;
        }
        else{
            returnObject = response;
        }

        System.out.println(returnObject.getClass());
        return ResponseEntity.badRequest().body(returnObject);
    }

}
