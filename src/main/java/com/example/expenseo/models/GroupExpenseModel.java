package com.example.expenseo.models;

import com.example.expenseo.enums.SplitExpenseType;
import com.example.expenseo.service.UserService;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "group_expenses")
public class GroupExpenseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false , updatable = false ,unique = true)
    private String id;

    @Column(nullable = false , precision = 12 ,scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SplitExpenseType splitExpenseType;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /// RELATIONSHIP 1: Which group does this expense belong to?
    @ManyToOne(fetch = FetchType.LAZY ,optional = false)
    @JoinColumn(name = "group_id" , nullable = false)
    private GroupModel group;

    /// RELATIONSHIP 2: Who pulled out their wallet and actually paid the total?
    @ManyToOne(fetch = FetchType.LAZY ,optional = false)
    @JoinColumn(name = "paid_by_user_id" , nullable = false)
    private UserModel paidBy;

    /// RELATIONSHIP 3: The Ledger. Who owes what for this specific bill?
    /// CascadeType.ALL means when we save the parent expense, it automatically saves all the splits!
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY ,orphanRemoval = true , mappedBy = "expense")
    private List<ExpenseSplitModel> splitModels = new ArrayList<>();
}
