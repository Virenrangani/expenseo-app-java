package com.example.expenseo.repository;

import com.example.expenseo.models.GroupExpenseModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupExpenseRepository extends JpaRepository<GroupExpenseModel, String> {
}
