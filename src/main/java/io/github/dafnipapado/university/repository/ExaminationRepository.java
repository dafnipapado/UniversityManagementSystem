package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.Examination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    Optional<Examination> findByCourseOfferingId(long id);
    Optional<Examination> findByCourseOfferingUuid(UUID courseOfferingUuid);
    Optional<Examination> findByUuid(UUID uuid);
}
