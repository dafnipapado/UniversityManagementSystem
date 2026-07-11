package io.github.dafnipapado.university.dto.userInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserInfoEditDTO(

        @NotBlank
        @Size(min = 2, max = 255)
        String firstname,

        @NotBlank
        @Size(min = 2, max = 255)
        String lastname,

        @NotBlank
        @Pattern(regexp = "\\d{9,}")
        String afm,

        @NotBlank
        @Pattern(regexp = "\\w+\\.?\\w+@\\w+\\.\\w+")
        String email,

        @NotBlank
        @Pattern(regexp = "\\d{10,}")
        String telephone,

        @NotBlank
        @Pattern(regexp = "\\d{5,}")
        String zipCode,

        @NotNull
        Long regionId
) {
}
