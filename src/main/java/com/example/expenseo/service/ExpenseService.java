package com.example.expenseo.service;

import com.example.expenseo.dto.ExpenseRequest;
import com.example.expenseo.dto.ExpenseResponse;
import com.example.expenseo.mapper.ExpenseMapper;
import com.example.expenseo.models.ExpenseModel;
import com.example.expenseo.models.UserModel;
import com.example.expenseo.repository.ExpenseRepository;
import com.example.expenseo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseResponse createExpense(ExpenseRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // In a real app, you might throw a custom UnauthorizedException here
            throw new RuntimeException("Authentication context is missing or invalid.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserModel currentUser = userDetails.getUser();

        // 2. Map the Request DTO to the Entity
        ExpenseModel newExpense = expenseMapper.toEntity(request);
        newExpense.setUser(currentUser);

        ExpenseModel saveExpense = expenseRepository.save(newExpense);

        return expenseMapper.toResponse(saveExpense);
    }

    public List<ExpenseResponse> getAllExpenses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()){
            throw new RuntimeException("Authentication context is missing or invalid.");
        }

        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        String userId = currentUser.getUser().getId();

        List<ExpenseModel> savedExpenses = expenseRepository
                .findByUserIdOrderByExpenseDateDesc(userId);

        return savedExpenses.stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
    }
}
