package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.model.*;
import io.github.dafnipapado.university.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilityServiceImpl implements IUtilityService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseOfferingRepository courseOfferingRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public User getUserByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username = " + username + " not found."));
    }

    @Override
    public Teacher getTeacherByUuid(UUID uuid) throws EntityNotFoundException {
        return teacherRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Active teacher with uuid = " + uuid + " not found."));
    }

    @Override
    public Student getStudentByStudentAmAndDeletedFalse(String studentAM) throws EntityNotFoundException {
        return studentRepository.findByStudentAMAndDeletedFalse(studentAM)
                .orElseThrow(() -> new EntityNotFoundException("Active student with AM = " + studentAM + " not found."));
    }

    @Override
    public Student getLoggedInStudent() throws EntityNotFoundException {
        User user = getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getStudent() == null || user.getStudent().isDeleted()) {
            throw new EntityNotFoundException("Active student with username = " + user.getUsername() + " not found.");
        }
        return user.getStudent();
    }

    @Override
    public Course getCourseByUuid(UUID uuid) throws EntityNotFoundException {
        return courseRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Active course with uuid = " + uuid + " not found."));
    }

    @Override
    public CourseOffering getCourseOfferingByUuidAndDeletedFalse(UUID uuid) throws EntityNotFoundException {
        return courseOfferingRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Active course offering with uuid = " + uuid + " not found."));
    }

    @Override
    public Enrollment getByStudentIdCourseOfferingId(Long studentId, Long courseOfferingId) throws EntityNotFoundException {
        return enrollmentRepository.findByStudentIdAndOfferingId(studentId, courseOfferingId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment for student with uuid = " + studentId +
                        " and course offering with uuid = " + courseOfferingId + " not found."));
    }

    @Override
    public boolean isEnrollmentExists(Enrollment enrollment) throws EntityAlreadyExistsException {
        return enrollmentRepository.findByStudentIdAndOfferingId(enrollment.getStudent().getId(), enrollment.getOffering().getId()).isPresent();
    }
}
