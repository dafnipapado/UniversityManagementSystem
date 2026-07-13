package io.github.dafnipapado.university.dto.student;

import io.github.dafnipapado.university.dto.user.UserEditDTO;
import io.github.dafnipapado.university.dto.userInfo.UserInfoEditDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record StudentEditDTO(

        @NotNull
        UUID uuid,

        @NotBlank
        @Pattern(regexp = "\\d{5,}")
        String studentAM,

        @NotNull
        UserEditDTO userEditDTO,

        @NotNull
        UserInfoEditDTO userInfoEditDTO
) {
}
