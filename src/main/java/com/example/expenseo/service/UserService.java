    package com.example.expenseo.service;

    import com.example.expenseo.dto.AuthResponse;
    import com.example.expenseo.dto.LoginRequest;
    import com.example.expenseo.dto.UserRequest;
    import com.example.expenseo.mapper.UserMapStructMapper;
    import com.example.expenseo.models.UserModel;
    import com.example.expenseo.repository.UserRepository;
    import com.example.expenseo.security.JwtUtils;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.bind.annotation.RequestBody;

    @Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapStructMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    public AuthResponse signUp(UserRequest user ) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw  new RuntimeException("Email is already in use!");
        }

        UserModel newUser =  userMapper.toEntity(user);

        String hasPassword = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(hasPassword);

        UserModel savedUser = userRepository.save(newUser);

        return userMapper.toResponse(savedUser);

    }

    public AuthResponse login( LoginRequest request){
        // 1. Authenticate the user
        // This single line does the heavy lifting: It checks if the user exists and if the password matches the hash.
        // If it fails, it throws a BadCredentialsException immediately.

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Fetch the user from the database (we know they exist because step 1 passed)
        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));

        String jwt = jwtUtils.generateToken((UserDetails) user);

        return AuthResponse.builder()
                .token(jwt)
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
