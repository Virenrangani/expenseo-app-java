package com.example.expenseo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateRequest {

    private String phoneNumber;
    private String gender;
    private String dob;
    private String profileImage;
}
