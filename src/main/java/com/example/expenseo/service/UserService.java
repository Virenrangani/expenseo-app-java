package com.example.expenseo.service;

import com.example.expenseo.dto.UserRequest;
import com.example.expenseo.dto.UserResponse;
import com.example.expenseo.mapper.UserMapStructMapper;
import com.example.expenseo.models.UserModel;
import com.example.expenseo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private UserMapStructMapper userMapper;


    public UserResponse signUp( UserRequest user ) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw  new RuntimeException("Email is already in use!");
        }

        UserModel newUser =  userMapper.toEntity(user);

        UserModel savedUser = userRepository.save(newUser);

        return userMapper.toResponse(savedUser);

    }
}
