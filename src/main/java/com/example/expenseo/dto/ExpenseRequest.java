package com.example.expenseo.dto;

import com.example.expenseo.enums.ExpenseCategory;
import com.example.expenseo.enums.ExpenseType;
import com.example.expenseo.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01" , message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Title is required")
    @Size(max = 100 , message = "Title cannot exceed 100 characters")
    private String title;

    @NotNull(message = "Transaction type (e.g., INCOME or EXPENSE) is required")
    private TransactionType transactionType;

    @NotNull(message = "Expense type is required")
    private ExpenseType expenseType;

    @NotNull(message = "Expense category is required")
    private ExpenseCategory expenseCategory;

}
