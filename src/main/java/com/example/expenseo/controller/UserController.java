package com.example.expenseo.controller;

import com.example.expenseo.dto.*;
import com.example.expenseo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid UserRequest user){

        AuthResponse response=userService.signUp(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody @Valid OtpVerificationRequest request){
        userService.verifyOtp(request);
        return ResponseEntity.ok("Email successfully verified! You can now log in.");
    }


    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody @Valid ResendOtpRequest request){
        userService.resendOtp(request);

        return ResponseEntity.ok("A fresh OTP has been sent to your email!");
    }
}
