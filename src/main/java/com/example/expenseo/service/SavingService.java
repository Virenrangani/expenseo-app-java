package com.example.expenseo.service;

import com.example.expenseo.dto.SavingRequest;
import com.example.expenseo.dto.SavingResponse;
import com.example.expenseo.mapper.SavingMapper;
import com.example.expenseo.models.SavingModel;
import com.example.expenseo.models.UserModel;
import com.example.expenseo.repository.SavingRepository;
import com.example.expenseo.repository.UserRepository;
import com.example.expenseo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavingService {

    private final SavingRepository savingRepository;
    private final SavingMapper savingMapper;
    private final UserRepository userRepository;

    public SavingResponse createSavingGoal(SavingRequest request){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getUser().getId();

        UserModel managedUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SavingModel savingGoal = SavingModel.builder()
                .goal(request.getGoal())
                .imageUrl(request.getImageUrl())
                .targetAmount(request.getTargetAmount())
                .user(managedUser)
                .build();

        SavingModel savedGoal = savingRepository.save(savingGoal);

        return savingMapper.toResponse(savedGoal);
    }

    public List<SavingResponse> GetAllSavingGoal(){

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        String userId = userDetails.getUser().getId();

        List<SavingModel> savingGoal = savingRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return savingGoal.stream().map(
                savingMapper::toResponse
        ).collect(Collectors.toList());
    }
}
