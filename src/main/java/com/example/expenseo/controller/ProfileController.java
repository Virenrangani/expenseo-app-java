package com.example.expenseo.controller;

import com.example.expenseo.dto.UserProfileDto;
import com.example.expenseo.dto.UserProfileUpdateRequest;
import com.example.expenseo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileDto> updateProfile(
            @PathVariable String userId,
            @RequestBody UserProfileUpdateRequest request) {

        UserProfileDto updatedProfile =
                userService.updateProfile(userId, request);

        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/{userId}")
    public  ResponseEntity<UserProfileDto> getProfile( @PathVariable String userId){
        UserProfileDto profile = userService.getProfile(userId);
        return  ResponseEntity.ok(profile);
    }
}
