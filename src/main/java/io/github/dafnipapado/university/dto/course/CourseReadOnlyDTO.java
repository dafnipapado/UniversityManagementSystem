package io.github.dafnipapado.university.dto.course;

import java.util.UUID;

public record CourseReadOnlyDTO(
        UUID uuid,
        String code,
        String name,
        String description,
        Integer ects,
        String departmentName
) {
}
