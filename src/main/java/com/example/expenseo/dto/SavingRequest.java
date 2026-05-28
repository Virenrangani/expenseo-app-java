package com.example.expenseo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingRequest {

    @NotBlank(message = "Gaol name is required")
    private String goal;

    @NotNull(message = "Target amount is required")
    private BigDecimal targetAmount;
}
