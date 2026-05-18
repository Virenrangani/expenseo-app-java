package com.example.expenseo.repository;

import com.example.expenseo.models.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupModel , String> {

    List<GroupModel> findByMembersIdOrderByCreatedAtDesc(String userId);
}
