package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByTeacherAM(String teacherAM);
    Optional<Teacher> findByUuidAndDeletedFalse(UUID uuid);

    @EntityGraph(attributePaths = {"user", "user.userInfo"})
    Page<Teacher> findAll(Pageable pageable);

    List<Teacher> findAllByOrderByTeacherAM();
}
