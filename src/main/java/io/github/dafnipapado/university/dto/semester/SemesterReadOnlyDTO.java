package io.github.dafnipapado.university.dto.semester;

import java.util.UUID;

public record SemesterReadOnlyDTO(
        UUID uuid,
        String name,
        Integer year,
        String startsAt,
        String endsAt,
        String registrationDeadline,
        boolean active
) {
}
