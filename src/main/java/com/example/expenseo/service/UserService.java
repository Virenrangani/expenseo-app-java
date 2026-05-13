    package com.example.expenseo.service;

    import com.example.expenseo.dto.AuthResponse;
    import com.example.expenseo.dto.LoginRequest;
    import com.example.expenseo.dto.UserRequest;
    import com.example.expenseo.mapper.UserMapStructMapper;
    import com.example.expenseo.models.UserModel;
    import com.example.expenseo.models.VerificationToken;
    import com.example.expenseo.repository.UserRepository;
    import com.example.expenseo.repository.VerificationTokenRepository;
    import com.example.expenseo.security.CustomUserDetails;
    import com.example.expenseo.security.JwtUtils;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.UUID;

    @Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapStructMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final VerificationTokenRepository tokenRepository;


    public AuthResponse signUp(UserRequest user ) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw  new RuntimeException("Email is already in use!");
        }

        UserModel newUser =  userMapper.toEntity(user);

        String hasPassword = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(hasPassword);

        newUser.setVerified(false);
        UserModel savedUser = userRepository.save(newUser);

        //Generate token
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(savedUser)
                .build();

        return userMapper.toResponse(savedUser);

    }

    // New method to handle the verification click
    public void verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        UserModel user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        // Clean up the used token
        tokenRepository.delete(verificationToken);
    }


        public AuthResponse login(LoginRequest request) {
            // 1. Authenticate the user AND capture the result
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // 2. Extract the CustomUserDetails from the authentication object
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 3. Extract the raw UserModel from your wrapper (assuming you added the getUser() method we discussed earlier)
            assert userDetails != null;
            UserModel user = userDetails.getUser();

            // 4. Generate the token
            String jwt = jwtUtils.generateToken(userDetails);

            // 5. Return the response
            return AuthResponse.builder()
                    .token(jwt)
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        }
    }
