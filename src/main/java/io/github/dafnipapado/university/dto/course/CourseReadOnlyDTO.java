package io.github.dafnipapado.university.dto.course;

public record CourseReadOnlyDTO(
        String uuid,
        String code,
        String name,
        String description,
        int ects,
        String departmentName
) {
}
