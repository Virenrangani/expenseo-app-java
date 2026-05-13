package com.example.expenseo.security;

import com.example.expenseo.models.UserModel;
import com.example.expenseo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {

        return username -> { UserModel user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                                    "User not found with email: " + username
                            ));

            return new CustomUserDetails(user);
        };
    }
}