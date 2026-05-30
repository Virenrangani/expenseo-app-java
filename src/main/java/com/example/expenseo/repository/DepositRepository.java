package com.example.expenseo.repository;

import com.example.expenseo.models.DepositModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<DepositModel,String> {
    List<DepositModel> findBySavingGoalIdOrderByCreatedAtDesc(String goalId);
}
