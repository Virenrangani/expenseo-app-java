package com.example.expenseo.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "group_split_expenses")
public class ExpenseSplitModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false , updatable = false)
    private String id;

    @Column(nullable = false , precision = 12 , scale = 2)
    private BigDecimal amountOwed;

    // Which user owes this money?
    @ManyToOne(fetch = FetchType.LAZY ,optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    // Which receipt does this split belong to?
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expense_id", nullable = false)
    private GroupExpenseModel expense;

}
