package com.example.expenseo.controller;

import com.example.expenseo.dto.DepositRequest;
import com.example.expenseo.dto.SavingResponse;
import com.example.expenseo.service.DepositService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saving")
public class DepositController {

    private final DepositService depositService;

    @PostMapping("/deposit")
    public ResponseEntity<SavingResponse> addGoalDeposit(@Valid @RequestBody DepositRequest request){
        SavingResponse response = depositService.addGoalDeposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
