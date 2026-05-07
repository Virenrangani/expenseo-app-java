package com.example.expenseo.controller;

import com.example.expenseo.dto.UserRequest;
import com.example.expenseo.dto.UserResponse;
import com.example.expenseo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid UserRequest user){

        UserResponse response=userService.signUp(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
