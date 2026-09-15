package io.github.dafnipapado.university.dto.semester;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

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
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startsAt,

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endsAt,

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime registrationDeadline,

        @NotNull
        boolean isActive

) {

    public static SemesterInsertDTO empty() {
        return new SemesterInsertDTO("", null, null, null, null, false);
    }

}
