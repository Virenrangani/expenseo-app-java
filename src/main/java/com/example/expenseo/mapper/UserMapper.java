package com.example.expenseo.mapper;

import com.example.expenseo.dto.UserRequest;
import com.example.expenseo.dto.UserResponse;
import com.example.expenseo.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public  static UserModel toEntity(UserRequest request){
        return UserModel.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .build();
    }

    public static UserResponse toResponse(UserModel response){
        return UserResponse.builder()
                .id(response.getId())
                .email(response.getEmail())
                .name(response.getName())
                .build();
    }
}
