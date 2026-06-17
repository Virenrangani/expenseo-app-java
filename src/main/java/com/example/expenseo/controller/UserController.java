package com.example.expenseo.controller;

import com.example.expenseo.dto.*;
import com.example.expenseo.models.RefreshTokenModel;
import com.example.expenseo.repository.RefreshTokenRepository;
import com.example.expenseo.security.JwtUtils;
import com.example.expenseo.service.RefreshTokenService;
import com.example.expenseo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

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

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdatePasswordRequest request){
        userService.updatePassword(request);

        return ResponseEntity.ok("Password is change successfully");
    }

    @PostMapping("/forgot-password")
    public  ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request){
        userService.forgetPassword(request);

        return  ResponseEntity.ok("Password reset code sent to your email!");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request){
        userService.resetPassword(request);

        return ResponseEntity.ok("Password successfully reset! You can now log in.");
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request){
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenModel::getUser)
                .map(user -> {
                    String newAccessToken = jwtUtils.generateToken((UserDetails) user);

                    return ResponseEntity.ok(RefreshTokenResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(requestRefreshToken)
                            .build());
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}
