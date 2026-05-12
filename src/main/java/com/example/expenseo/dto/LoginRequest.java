package com.example.expenseo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Please provide a valid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain letters , special character and numbers"
    )
    private String password;
}
