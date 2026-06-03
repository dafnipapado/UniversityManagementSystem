package io.github.dafnipapado.university.dto;

public record StudentInsertDTO(String firstname, String lastname, String studentAM, String afm, String email, String telephone, String zipCode, Long regionId, String username, String password, Long roleId) {

    public static StudentInsertDTO empty() {
        return new StudentInsertDTO("", "", "", "", "", "", "", 0L, "", "", 0L);
    }
}
