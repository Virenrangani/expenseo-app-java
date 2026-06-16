package com.example.expenseo.repository;

import com.example.expenseo.models.RefreshTokenModel;
import com.example.expenseo.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel,Long> {

    Optional<RefreshTokenModel> findByToken(String token);
    int deleteByUser(UserModel user);
}
