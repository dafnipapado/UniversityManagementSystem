package io.github.dafnipapado.university.dto.semester;

import jakarta.validation.constraints.*;

public record SemesterInsertDTO(

        @NotBlank
        String name,

        @NotNull
        @Min(1900)
        @Max(2100)
        Integer year
) {

    public static SemesterInsertDTO empty() {
        return new SemesterInsertDTO("", null);
    }

}
