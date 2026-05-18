package com.example.expenseo.repository;

import com.example.expenseo.models.ExpenseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseModel, String> {

    List<ExpenseModel> findByUserIdOrderByExpenseDateDesc(String userId);

    Optional<ExpenseModel> findByIdAndUserId(String id , String userId);
}
