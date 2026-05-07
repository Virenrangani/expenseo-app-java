package com.example.expenseo.repository;

import com.example.expenseo.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel,String> {

    boolean existsByEmail(String email);
}
