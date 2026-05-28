package com.example.expenseo.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "saving_goal_deposits")
public class DepositModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true , nullable = false , updatable = false)
    private String id;

    @Column(nullable = false , precision = 12 , scale = 2)
    private BigDecimal savedAmount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "user_id" , nullable = false)
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "goal_id" , nullable = false)
    private SavingModel savingGoal;
}
