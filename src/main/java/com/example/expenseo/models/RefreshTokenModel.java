package com.example.expenseo.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "refresh_token")
public class RefreshTokenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private  String token;

    @Column(nullable = false)
    private Instant expireTime;

    private boolean revoked;

    private String deviceId;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserModel user;
}
