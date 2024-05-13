package com.talenttrack.repository;

import com.talenttrack.entities.Role;
import com.talenttrack.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByRoleIn(List<Role> roles);
    List<User> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

}
