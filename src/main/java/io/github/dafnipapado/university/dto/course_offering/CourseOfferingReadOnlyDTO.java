package io.github.dafnipapado.university.dto.course_offering;

import io.github.dafnipapado.university.dto.course.CourseReadOnlyDTO;
import io.github.dafnipapado.university.dto.semester.SemesterReadOnlyDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherReadOnlyDTO;

public record CourseOfferingReadOnlyDTO(
        String uuid,
        CourseReadOnlyDTO courseReadOnlyDTO,
        TeacherReadOnlyDTO teacherReadOnlyDTO,
        SemesterReadOnlyDTO semesterReadOnlyDTO,
        Boolean isStudentEnrolled
) {
}
