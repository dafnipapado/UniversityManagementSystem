package io.github.dafnipapado.university.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserInsertDTO(
        @NotBlank
        @Size(min = 3, max = 255)
        String username,

        @NotBlank
        @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&+=])^.{8,}$")
        String password,

        @NotNull
        Long roleId) {

    public static UserInsertDTO empty() {
        return new UserInsertDTO("", "", 0L);
    };
}
