package com.example.expenseo.controller;

import com.example.expenseo.dto.DepositRequest;
import com.example.expenseo.dto.DepositResponse;
import com.example.expenseo.dto.SavingResponse;
import com.example.expenseo.service.DepositService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/deposit/{goalId}")
    public ResponseEntity<List<DepositResponse>> getAllDeposit(@PathVariable String goalId){
        List<DepositResponse> responses =depositService.getAllDeposit(goalId);

        return ResponseEntity.ok(responses);
    }
}
