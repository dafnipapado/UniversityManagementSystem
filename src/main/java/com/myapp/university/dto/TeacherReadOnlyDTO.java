package com.myapp.university.dto;

import java.util.UUID;

public record TeacherReadOnlyDTO(UUID uuid, String firstname, String lastname, String teacherAm) {
}
