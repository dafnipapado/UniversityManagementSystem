package com.myapp.university.dto;

public record UserInsertDTO(String username, String password, Long roleId) {

    public static UserInsertDTO empty() {
        return new UserInsertDTO("", "", 0L);
    };
}
