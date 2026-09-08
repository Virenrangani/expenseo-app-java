package com.example.expenseo.service;


import com.example.expenseo.dto.DeviceTokenRequest;
import com.example.expenseo.models.UserDevice;
import com.example.expenseo.repository.UserDeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeviceTokenService {

    private final UserDeviceRepository userDeviceRepository;

    public void registerOrUpdateDevice(String userId, DeviceTokenRequest request) {
        final String token = request.getFcmToken().trim();
        final String type = request.getDeviceType().trim().toUpperCase();

        userDeviceRepository.findByFcmToken(token).ifPresentOrElse(
                existingDevice -> {
                    existingDevice.setUserId(userId);
                    existingDevice.setDeviceType(type);
                    existingDevice.setUpdatedAt(LocalDateTime.now());
                    userDeviceRepository.save(existingDevice);
                    log.info("Updated FCM token for user: {}", userId);
                },
                () -> {
                    UserDevice newDevice = UserDevice.builder()
                            .userId(userId)
                            .fcmToken(token)
                            .deviceType(type)
                            .updatedAt(LocalDateTime.now())
                            .build();

                    userDeviceRepository.save(newDevice);
                    log.info("Registered new FCM token for user: {}", userId);
                }
        );
    }

    public void removeDeviceToken(String fcmToken) {
        if (fcmToken != null && !fcmToken.isBlank()) {
            userDeviceRepository.deleteByFcmToken(fcmToken.trim());
            log.info("Deleted FCM token: {}", fcmToken);
        }
    }
}