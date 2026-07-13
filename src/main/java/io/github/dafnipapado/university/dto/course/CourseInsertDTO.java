package io.github.dafnipapado.university.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CourseInsertDTO(

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}[0-9]{3}$")
        String code,

        @NotBlank
        @Size(min = 3, max = 255)
        String name,

        @Size(min = 20)
        String description,

        @NotNull
        int ects,

        @NotNull
        Long departmentId
) {

    public static CourseInsertDTO empty() {
        return new CourseInsertDTO("", "", "", 0, 0L);
    }
}
