package com.example.expenseo.repository;

import com.example.expenseo.models.GroupExpenseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupExpenseRepository extends JpaRepository<GroupExpenseModel, String> {
    // Fetches all expenses for a group, newest first!
    List<GroupExpenseModel> findByGroupIdOrderByCreatedAtDesc(String groupId);
}
