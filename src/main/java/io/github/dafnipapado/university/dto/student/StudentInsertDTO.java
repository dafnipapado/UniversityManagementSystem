package io.github.dafnipapado.university.dto.student;

import io.github.dafnipapado.university.dto.userInfo.UserInfoInsertDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record StudentInsertDTO(

        @NotBlank
        @Pattern(regexp = "\\d{5}")
        String studentAM,

        @NotNull
        UserInfoInsertDTO userInfoInsertDTO
) {

    public static StudentInsertDTO empty() {
        return new StudentInsertDTO("", UserInfoInsertDTO.empty());
    }
}
