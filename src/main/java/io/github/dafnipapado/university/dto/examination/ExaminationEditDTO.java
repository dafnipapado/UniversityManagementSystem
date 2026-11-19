package io.github.dafnipapado.university.dto.examination;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ExaminationEditDTO(

        @NotNull
        UUID uuid,

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate examinationDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
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
}
