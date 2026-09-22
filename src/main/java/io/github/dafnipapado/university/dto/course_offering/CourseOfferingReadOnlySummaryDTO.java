package io.github.dafnipapado.university.dto.course_offering;

public record CourseOfferingReadOnlySummaryDTO(
        String courseCode,
        String courseName,
        String semesterName,
        String semesterYear
) {
}
