package com.example.expenseo.repository;

import com.example.expenseo.models.SavingModel;
import com.example.expenseo.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingRepository extends JpaRepository<SavingModel,String> {
    List<SavingModel> findByUserIdOrderByCreatedAtDesc(String userId);
}
