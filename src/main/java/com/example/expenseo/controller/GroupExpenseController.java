package com.example.expenseo.controller;

import com.example.expenseo.dto.GroupExpenseRequest;
import com.example.expenseo.dto.GroupExpenseResponse;
import com.example.expenseo.service.GroupExpenseService;
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
@RequestMapping("api/group-expenses")
public class GroupExpenseController {

    private final GroupExpenseService groupExpenseService;

    @PostMapping
    public ResponseEntity<GroupExpenseResponse> createGroupExpense(
            @Valid @RequestBody GroupExpenseRequest request) {

        GroupExpenseResponse response = groupExpenseService.createGroupExpense(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
