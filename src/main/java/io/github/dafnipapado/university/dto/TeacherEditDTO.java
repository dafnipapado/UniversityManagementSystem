package io.github.dafnipapado.university.dto;

import java.util.UUID;

public record TeacherEditDTO (UUID uuid, String teacherAM, String firstname, String lastname, String afm, String email, String telephone, String zipCode, Long regionId, String username, String password) {

}
