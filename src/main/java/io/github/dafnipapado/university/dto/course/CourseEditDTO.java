package io.github.dafnipapado.university.dto.course;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record CourseEditDTO(

        @NotNull
        UUID uuid,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}[0-9]{3}$")
        String code,

        @NotBlank
        @Size(min = 3, max = 255)
        String name,

        @Size(min = 20)
        String description,

        @NotNull
        @Min(1)
        @Max(10)
        Integer ects,

        @NotNull
        Long departmentId
) {
}
