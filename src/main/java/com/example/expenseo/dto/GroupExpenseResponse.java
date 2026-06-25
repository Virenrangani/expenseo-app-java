package com.example.expenseo.dto;

import com.example.expenseo.enums.SplitExpenseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupExpenseResponse {
    private String id;
    private BigDecimal amount;
    private String title;
    private SplitExpenseType splitExpenseType;
    private LocalDateTime createdAt;
    private String groupId;
    private String paidByUserId;
    private String paidByUserName;
    private List<SplitDetailResponse> splits;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SplitDetailResponse {
        private String id;
        private String userId;
        private BigDecimal amountOwed;
    }
}
