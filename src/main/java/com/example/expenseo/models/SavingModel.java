package com.example.expenseo.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "saving_goals")
@Getter
@Setter
public class SavingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false,unique = true)
    private String id;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false , precision = 12 , scale = 2)
    private BigDecimal targetAmount;

    @Column(nullable = false , precision = 12 ,scale = 2)
    @Builder.Default
    private BigDecimal savingAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isCompleted = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id" , nullable = false)
    private UserModel user;

}
