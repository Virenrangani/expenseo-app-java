package com.example.expenseo.service;

import com.example.expenseo.models.RefreshTokenModel;
import com.example.expenseo.models.UserModel;
import com.example.expenseo.repository.RefreshTokenRepository;
import com.example.expenseo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDurationMs;

    public RefreshTokenModel generateToken(String userId){

        RefreshTokenModel refreshToken = RefreshTokenModel.builder()
                .user(userRepository.findById(userId).orElseThrow(()->new RuntimeException("User is not found")))
                .token(generateSecureToken())
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
    public void deleteByUserId(String userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenRepository.deleteByUser(user);
    }

    public Optional<RefreshTokenModel> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[64];

        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
