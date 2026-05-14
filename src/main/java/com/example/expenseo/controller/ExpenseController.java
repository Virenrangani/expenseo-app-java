package com.example.expenseo.controller;

import com.example.expenseo.dto.ExpenseRequest;
import com.example.expenseo.dto.ExpenseResponse;
import com.example.expenseo.models.ExpenseModel;
import com.example.expenseo.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping()
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request
            ){
        ExpenseResponse response = expenseService.createExpense(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<ExpenseResponse>> getAllExpense(){
        List<ExpenseResponse> responses = expenseService.getAllExpenses();
        return ResponseEntity.ok(responses);
    }
}
