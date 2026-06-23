package com.example.expenseo.controller;

import com.example.expenseo.dto.GroupRequest;
import com.example.expenseo.dto.GroupResponse;
import com.example.expenseo.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody GroupRequest request){
        GroupResponse response = groupService.createGroup(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups(){
        List<GroupResponse> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable String id){
        groupService.deleteGroup(id);
        return ResponseEntity.ok("Group Deleted Successfully");
    }
}
