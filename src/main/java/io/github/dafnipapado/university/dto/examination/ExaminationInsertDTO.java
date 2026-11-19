package io.github.dafnipapado.university.dto.examination;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ExaminationInsertDTO(

        @NotNull
        LocalDate examinationDate,

        LocalTime startTime,

        @NotNull
        @Min(1)
        Integer duration,

        @NotNull
        @Min(1)
        Integer passingGrade,

        @NotNull
        UUID courseOfferingUuid
) {
    public static ExaminationInsertDTO empty(UUID uuid) {
        return new ExaminationInsertDTO(null, null, null, null, uuid);
    }
}
