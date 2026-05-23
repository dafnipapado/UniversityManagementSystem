package com.myapp.university.dto;

import java.util.UUID;

public record TeacherReadOnlyDTO (String uuid, String firstname, String lastname, String teacherAm) {
}
