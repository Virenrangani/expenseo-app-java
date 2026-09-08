package com.example.expenseo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTokenRequest {

    @NotBlank(message = "FCM token must not be blank")
    private String fcmToken;

    @NotBlank(message = "Device type must not be blank")
    @Pattern(regexp = "^(ANDROID|IOS|WEB)$", message = "Device type must be ANDROID, IOS, or WEB")
    private String deviceType;
}