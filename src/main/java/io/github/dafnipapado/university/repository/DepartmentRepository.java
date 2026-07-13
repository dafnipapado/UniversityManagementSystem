package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.static_data.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByOrderByNameAsc();
    Optional<Department> findByName(String name);
}
