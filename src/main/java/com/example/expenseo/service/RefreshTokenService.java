package com.example.expenseo.service;

import com.example.expenseo.models.RefreshTokenModel;
import com.example.expenseo.repository.RefreshTokenRepository;
import com.example.expenseo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;


    public RefreshTokenModel generateToken(String userId){
        long refreshTokenDurationMs = 604800000L;
        RefreshTokenModel refreshToken = RefreshTokenModel.builder()
                .user(userRepository.findById(userId).orElseThrow(()->new RuntimeException("User is not found")))
                .token(UUID.randomUUID().toString())
                .expireTime(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshTokenModel verifyExpiration(RefreshTokenModel token) {
        if (token.getExpireTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(String userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
