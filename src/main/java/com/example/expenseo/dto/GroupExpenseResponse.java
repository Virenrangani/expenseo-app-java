package com.example.expenseo.dto;

import com.example.expenseo.enums.SplitExpenseType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GroupExpenseResponse {
    private String id;
    private BigDecimal amount;
    private String title;
    private SplitExpenseType splitExpenseType;
    private LocalDateTime createdAt;
    private String groupId;
    private String paidByUserId;
    private List<SplitDetailResponse> splits;

    @Data
    @Builder
    public static class SplitDetailResponse {
        private String id;
        private String userId;
        private BigDecimal amountOwed;
    }
}
