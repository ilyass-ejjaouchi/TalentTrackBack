package com.talenttrack.repository;

import com.talenttrack.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.id = :userId")
    List<Project> findByUserId(Integer userId);
}