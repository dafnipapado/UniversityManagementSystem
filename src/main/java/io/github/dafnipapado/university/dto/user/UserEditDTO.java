package io.github.dafnipapado.university.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserEditDTO(

        @NotBlank
        @Size(min = 3, max = 255)
        String username,

        @NotBlank
        @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&+=])^.{8,}$")
        String password
) {
}
