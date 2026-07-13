package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);
    Optional<Course> findByUuid(UUID uuid);
}
