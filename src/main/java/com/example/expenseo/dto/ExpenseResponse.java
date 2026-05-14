package com.example.expenseo.dto;

import com.example.expenseo.enums.ExpenseCategory;
import com.example.expenseo.enums.ExpenseType;
import com.example.expenseo.enums.TransactionType;
import com.example.expenseo.models.UserModel;
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
public class ExpenseResponse {

    private String id;

    private BigDecimal amount;

    private String title;

    private TransactionType transactionType;

    private ExpenseType expenseType;

    private ExpenseCategory expenseCategory;

    private LocalDateTime createdAt;

    private String userId;

}
