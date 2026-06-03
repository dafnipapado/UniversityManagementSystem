package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.static_data.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByOrderByNameAsc();
}
