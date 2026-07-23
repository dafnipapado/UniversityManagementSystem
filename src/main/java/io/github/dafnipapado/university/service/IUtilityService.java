package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.model.*;

import java.util.UUID;

public interface IUtilityService {
    User getUserByUsername(String username) throws EntityNotFoundException;
    Teacher getTeacherByUuid(UUID uuid) throws EntityNotFoundException;
    Student getStudentByStudentAmAndDeletedFalse(String studentAM) throws EntityNotFoundException;
    Student getLoggedInStudent() throws EntityNotFoundException;
    Course getCourseByUuid(UUID uuid) throws EntityNotFoundException;
    CourseOffering getCourseOfferingByUuidAndDeletedFalse(UUID uuid) throws EntityNotFoundException;
    Enrollment getByStudentIdCourseOfferingId(Long studentId, Long courseOfferingId) throws EntityNotFoundException;
    boolean isEnrollmentExists(Enrollment enrollment) throws EntityAlreadyExistsException;
}
