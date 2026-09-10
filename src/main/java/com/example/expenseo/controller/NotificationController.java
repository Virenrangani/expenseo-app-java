package com.example.expenseo.controller;

import com.example.expenseo.dto.NotificationSendRequest;
import com.example.expenseo.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody NotificationSendRequest request) {
        notificationService.sendNotificationToUsers(request);
        return ResponseEntity.ok().build();
    }
}
