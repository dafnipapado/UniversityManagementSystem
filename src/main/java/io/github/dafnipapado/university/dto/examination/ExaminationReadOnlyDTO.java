package io.github.dafnipapado.university.dto.examination;

import java.time.LocalDate;
import java.time.LocalTime;

public record ExaminationReadOnlyDTO(
        LocalDate examinationDate,
        LocalTime startTime,
        Integer duration,
        Integer passingGrade,
        String courseName,
        String courseCode
) {
}
