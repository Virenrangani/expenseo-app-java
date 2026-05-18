package com.example.expenseo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "expense_groups")
public class GroupModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true , nullable = false)
    private String id;

    @Column(nullable = false)
    @Size(max = 100)
    private String name;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "groups_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default /// Required so Lombok doesn't overwrite our new HashSet
    private Set<UserModel> members = new HashSet<>();
}
