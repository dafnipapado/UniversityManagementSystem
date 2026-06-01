package com.myapp.university.repository;

import com.myapp.university.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"role", "role.capabilities"})
    Optional<User> findByUsername(String username);
}
