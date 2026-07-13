package io.github.dafnipapado.university.dto.teacher;

import io.github.dafnipapado.university.dto.user.UserEditDTO;
import io.github.dafnipapado.university.dto.userInfo.UserInfoEditDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record TeacherEditDTO (

        @NotNull
        UUID uuid,

        @NotBlank
        @Pattern(regexp = "\\d{5}")
        String teacherAM,

        @NotNull
        UserEditDTO userEditDTO,

        @NotNull
        UserInfoEditDTO userInfoEditDTO

) {

}
