package com.example.expenseo.controller;

import com.example.expenseo.dto.SavingRequest;
import com.example.expenseo.dto.SavingResponse;
import com.example.expenseo.service.SavingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/saving")
public class SavingController {

    private final SavingService savingService;

    @PostMapping()
    public ResponseEntity<SavingResponse> createSavingGoal(@Valid @RequestBody SavingRequest request){
        SavingResponse response = savingService.createSavingGoal(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<SavingResponse>> getAllSavingGoal(){
        List<SavingResponse> responses = savingService.GetAllSavingGoal();

        return ResponseEntity.ok(responses);
    }
}
