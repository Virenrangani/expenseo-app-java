package com.example.expenseo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingResponse {

    private String id;

    private String goal;

    private String imageUrl;

    private BigDecimal targetAmount;

    private BigDecimal savingAmount = BigDecimal.ZERO;

    private Boolean isCompleted = false;

    private LocalDateTime createdAt;

    private String  userId;
}
