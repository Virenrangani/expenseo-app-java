package com.example.expenseo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSendRequest {

    @NotEmpty(message = "Recipient userIds list cannot be empty")
    private List<String> userIds;

    @NotBlank(message = "Notification title cannot be blank")
    private String title;

    @NotBlank(message = "Notification body cannot be blank")
    private String body;

    private Map<String, String> data;
}