package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.static_data.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByOrderByNameAsc();
    Optional<Role> findByName(String name);
}
