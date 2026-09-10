package com.example.expenseo.service;

import com.example.expenseo.dto.NotificationSendRequest;
import com.example.expenseo.models.UserDevice;
import com.example.expenseo.repository.UserDeviceRepository;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final UserDeviceRepository userDeviceRepository;

    public void sendNotificationToUsers(NotificationSendRequest request) {
        final List<String> targetUserIds = request.getUserIds();
        if (targetUserIds == null || targetUserIds.isEmpty()) {
            return;
        }

        // 1. Fetch registered tokens for target users
        List<UserDevice> devices = userDeviceRepository.findAllByUserIdIn(targetUserIds);
        if (devices.isEmpty()) {
            log.warn("No active FCM tokens found for userIds: {}", targetUserIds);
            return;
        }

        List<String> staleTokens = new ArrayList<>();
        Map<String, String> payloadData = request.getData() != null ? request.getData() : Map.of();

        // 2. Dispatch notifications
        for (UserDevice device : devices) {
            Message message = Message.builder()
                    .setToken(device.getFcmToken())
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody())
                            .build())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .setNotification(AndroidNotification.builder()
                                    .setChannelId("expenseo_high_importance_channel")
                                    .setSound("default")
                                    .build())
                            .build())
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setSound("default")
                                    .setBadge(1)
                                    .build())
                            .build())
                    .putAllData(payloadData)
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                log.info("FCM message sent successfully to user {}: {}", device.getUserId(), response);
            } catch (FirebaseMessagingException e) {
                log.error("FCM dispatch error for token {} (user {}): {}",
                        device.getFcmToken(), device.getUserId(), e.getMessage());

                // Auto-clean tokens that are no longer valid
                if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED ||
                        e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
                    staleTokens.add(device.getFcmToken());
                }
            }
        }

        // 3. Remove stale tokens in batch
        if (!staleTokens.isEmpty()) {
            userDeviceRepository.deleteAllByFcmTokenIn(staleTokens);
            log.info("Removed {} stale/unregistered tokens from database", staleTokens.size());
        }
    }
}
