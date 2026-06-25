package com.example.expenseo.dto;

import com.example.expenseo.enums.SplitExpenseType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupExpenseRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Split type is required")
    private SplitExpenseType splitExpenseType;

    @NotBlank(message = "Group id is required")
    private String groupId;

    @NotBlank(message = "Paid by User ID is required")
    private String paidByUserId;

    @NotBlank(message = "Paid by user name is required")
    private String paidByUserName;

    @NotEmpty(message = "Splits cannot be empty")
    @Valid
    private List<SplitDetailRequest> splits;


    // --- INNER CLASS FOR THE SPLITS ---
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SplitDetailRequest {

        @NotBlank(message = "User ID is required")
        private String userId;

        @NotNull(message = "Amount owed is required")
        @DecimalMin(value = "0.00", message = "Amount owed cannot be negative")
        private BigDecimal amountOwed;
    }

}
