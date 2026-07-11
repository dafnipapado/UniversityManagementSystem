package io.github.dafnipapado.university.dto.teacher;

import io.github.dafnipapado.university.dto.userInfo.UserInfoInsertDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record TeacherInsertDTO (
        @NotBlank
        @Pattern(regexp = "\\d{5,}")
        String teacherAM,

        @NotNull
        UserInfoInsertDTO userInfoInsertDTO
){

    public static TeacherInsertDTO empty() {
        return new TeacherInsertDTO("", UserInfoInsertDTO.empty());
    }
}
