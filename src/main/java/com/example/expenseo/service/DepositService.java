package com.example.expenseo.service;

import com.example.expenseo.dto.DepositRequest;
import com.example.expenseo.dto.SavingResponse;
import com.example.expenseo.mapper.DepositMapper;
import com.example.expenseo.mapper.SavingMapper;
import com.example.expenseo.models.DepositModel;
import com.example.expenseo.models.SavingModel;
import com.example.expenseo.models.UserModel;
import com.example.expenseo.repository.DepositRepository;
import com.example.expenseo.repository.SavingRepository;
import com.example.expenseo.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final SavingRepository savingRepository;
    private final DepositMapper depositMapper;
    private final SavingMapper savingMapper;

    @Transactional
    public SavingResponse addGoalDeposit(DepositRequest request){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        UserModel currentUser = userDetails.getUser();

        SavingModel goal = savingRepository.findById(request.getSavingGoalId())
                .orElseThrow(() -> new RuntimeException("Saving goal not found"));

        if (!goal.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("Unauthorized: You do not own this saving goal.");
        }

        if (goal.getIsCompleted()){
            throw new RuntimeException("This goal is already completed!");
        }

        DepositModel deposit = depositMapper.toEntity(request);

        DepositModel savedDeposit = depositRepository.save(deposit);

        BigDecimal newAmount =  goal.getSavingAmount().add(request.getSavedAmount());
        goal.setSavingAmount(newAmount);

        if (newAmount.compareTo(goal.getTargetAmount()) >= 0) {
            goal.setIsCompleted(true);
        }

        SavingModel updatedGoal = savingRepository.saveAndFlush(goal);

        return savingMapper.toResponse(updatedGoal);
    }
}
