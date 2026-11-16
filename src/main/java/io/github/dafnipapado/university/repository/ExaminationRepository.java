package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.Examination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {
}
