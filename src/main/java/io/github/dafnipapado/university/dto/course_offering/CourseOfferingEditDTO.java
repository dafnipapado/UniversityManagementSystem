package io.github.dafnipapado.university.dto.course_offering;

import java.util.UUID;

public record CourseOfferingEditDTO(
        UUID uuid,
        UUID courseUuid,
        UUID teacherUuid,
        Long semesterId
) {
}
