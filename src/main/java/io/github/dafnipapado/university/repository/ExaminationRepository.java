package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.Examination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    Optional<Examination> findByCourseOfferingId(long id);
}
