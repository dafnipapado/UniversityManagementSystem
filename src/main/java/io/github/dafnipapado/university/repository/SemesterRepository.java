package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    List<Semester> findAllByOrderByYearAscNameAsc();
}
