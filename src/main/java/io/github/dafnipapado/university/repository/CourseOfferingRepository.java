package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.CourseOffering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseOfferingRepository extends JpaRepository<CourseOffering, Long> {
    Optional<CourseOffering> findByUuidAndDeletedFalse(UUID UUID);
    boolean existsByCourseUuidAndTeacherUuidAndSemesterUuid(UUID courseUuid, UUID teacherUuid, UUID semesterUuid);
    Page<CourseOffering> findAllByDeletedFalse(Pageable pageable);
    long countByDeletedFalse();
    Optional<CourseOffering> findTopByOrderByIdDesc();
    List<CourseOffering> findByTeacherUuidAndSemesterUuidAndDeletedFalse(UUID teacherUuid, UUID semesterUuid);
    Optional<CourseOffering> findTopByTeacherUuidAndDeletedFalseOrderByIdDesc(UUID uuid);

    @Query(value = """
        SELECT COUNT(DISTINCT e.student)
        FROM Enrollment e
        WHERE e.offering.teacher.uuid = :teacherUuid
        AND e.offering.semester.uuid = :semesterUuid
        """)
    long countIndividualStudents(UUID teacherUuid, UUID semesterUuid);

    @Query(value = """
            SELECT SUM(co.course.hoursPerWeek)
            FROM CourseOffering co
            WHERE co.teacher.uuid = :teacherUuid AND co.course.deleted = false
            """)
    long sumTotalHours(UUID teacherUuid);
}
