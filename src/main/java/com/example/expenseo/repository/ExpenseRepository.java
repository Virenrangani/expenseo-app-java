package com.example.expenseo.repository;

import com.example.expenseo.models.ExpenseModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<ExpenseModel, String> {
}
