package io.github.dafnipapado.university.dto.course_offering;

import java.util.UUID;

public record CourseOfferingInsertDTO(
        UUID courseUuid,
        UUID teacherUuid,
        UUID semesterUuid
) {

    public static CourseOfferingInsertDTO empty() {
        return new CourseOfferingInsertDTO(null, null,  null);
    }
}
