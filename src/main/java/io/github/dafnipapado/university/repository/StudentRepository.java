package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentAM(String studentAM);
    Optional<Student> findByUuid(UUID uuid);
    Optional<Student> findByUuidAndDeletedFalse(UUID uuid);
    Optional<Student> findByStudentAmAndDeletedFalse(String StudentAM);
}
