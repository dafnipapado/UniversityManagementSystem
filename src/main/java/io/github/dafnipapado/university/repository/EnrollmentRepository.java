package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentIdAndOfferingId(Long studentId, Long courseOfferingId);
}
