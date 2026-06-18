package com.example.expenseo.models;

import com.example.expenseo.enums.ExpenseCategory;
import com.example.expenseo.enums.ExpenseType;
import com.example.expenseo.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses" ,indexes = {
        @Index(name = "idx_expense_user", columnList = "user_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false ,updatable = false)
    private String id;

    @Column(nullable = false , precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false,length = 100)
    private String title;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "transaction_type", length = 50)
    private TransactionType transactionType;

    @Enumerated(value = EnumType.STRING)
    private ExpenseType expenseType;

    @Enumerated(value = EnumType.STRING)
    private ExpenseCategory expenseCategory;

    @CreationTimestamp
    private LocalDateTime expenseDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false , foreignKey = @ForeignKey(name = "fk_expense_user"))
    private UserModel user;
}
