package com.example.expenseo.controller;

import com.example.expenseo.dto.DeviceTokenRequest;
import com.example.expenseo.service.DeviceTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/fcm-token")
@RequiredArgsConstructor
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;

    @PostMapping
    public ResponseEntity<Void> registerToken(
            @Valid @RequestBody DeviceTokenRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        final String userId = userDetails.getUsername();
        deviceTokenService.registerOrUpdateDevice(userId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteToken(@RequestBody DeviceTokenRequest request) {
        deviceTokenService.removeDeviceToken(request.getFcmToken());
        return ResponseEntity.noContent().build();
    }
}