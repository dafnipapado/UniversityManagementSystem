package io.github.dafnipapado.university.dto.semester;

public record SemesterReadOnlyDTO(
        String name,
        Integer year,
        String startsAt,
        String endsAt,
        String registrationDeadline,
        boolean active
) {
}
