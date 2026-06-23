package com.example.expenseo.repository;

import com.example.expenseo.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel,String> {

    boolean existsByEmail(String email);

    Optional<UserModel> findByEmail(String email);

    List<UserModel> findByEmailIn(List<String> emails);

}
