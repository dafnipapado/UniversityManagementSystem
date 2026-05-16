package com.myapp.university.dto;

import java.util.UUID;

public record TeacherInsertDTO (String firstname, String lastname, String teacherAM, String afm, String email, String telephone, String zipCode, Long regionId, String username, String password, Long roleId){

    public static TeacherInsertDTO empty() {
        return new TeacherInsertDTO("", "", "", "", "", "","", 0L, "", "", 0L);
    }
}
