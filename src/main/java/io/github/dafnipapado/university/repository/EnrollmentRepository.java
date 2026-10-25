package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentIdAndOfferingId(Long studentId, Long courseOfferingId);
    long countByOfferingUuid(UUID uuid);
    List<Enrollment> findByStudentUuidAndOfferingSemesterUuidAndOfferingDeletedFalse(UUID studentUuid, UUID semesterUuid);
}
