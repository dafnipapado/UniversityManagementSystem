package io.github.dafnipapado.university.dto.semester;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SemesterInsertDTO(

        @NotBlank
        String name,

        @NotNull
        @Min(1900)
        @Max(2100)
        Integer year,

        @NotNull
        LocalDate startsAt,

        @NotNull
        LocalDate endsAt,

        @NotNull
        LocalDateTime registrationDeadline,

        @NotNull
        boolean isActive

) {

    public static SemesterInsertDTO empty() {
        return new SemesterInsertDTO("", null, null, null, null, false);
    }

}
