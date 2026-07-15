package io.github.dafnipapado.university.dto.teacher;

import java.util.UUID;

public record TeacherReadOnlyDTO (
        UUID uuid,
        String firstname,
        String lastname,
        String teacherAM) {
}
