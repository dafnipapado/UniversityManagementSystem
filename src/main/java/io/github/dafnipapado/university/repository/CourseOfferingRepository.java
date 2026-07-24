package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.CourseOffering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CourseOfferingRepository extends JpaRepository<CourseOffering, Long> {
    Optional<CourseOffering> findByUuidAndDeletedFalse(UUID UUID);
    boolean existsByCourseUuidAndTeacherUuidAndSemesterId(UUID courseUuid, UUID teacherUuid, Long semesterId);
    Page<CourseOffering> findAllByDeletedFalse(Pageable pageable);
}
