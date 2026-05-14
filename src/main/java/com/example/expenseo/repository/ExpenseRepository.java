package com.example.expenseo.repository;

import com.example.expenseo.models.ExpenseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseModel, String> {

    List<ExpenseModel> findByUserIdOrderByExpenseDateDesc(String userId);
}
