package com.example.expenseo.mapper;

import com.example.expenseo.dto.UserRequest;
import com.example.expenseo.dto.AuthResponse;
import com.example.expenseo.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapStructMapper {

    UserModel toEntity(UserRequest request);

    AuthResponse toResponse(UserModel user);
}
