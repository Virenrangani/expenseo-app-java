package com.example.expenseo.repository;

import com.example.expenseo.models.DepositModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<DepositModel,String> {
}
