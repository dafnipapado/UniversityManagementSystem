package io.github.dafnipapado.university.dto.examination;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ExaminationReadOnlyDTO(
        LocalDate examinationDate,
        LocalTime startTime,
        Integer duration,
        Integer passingGrade,
        String examMonth,
        String courseName,
        String courseCode,
        UUID courseOfferingUuid
) {
}
