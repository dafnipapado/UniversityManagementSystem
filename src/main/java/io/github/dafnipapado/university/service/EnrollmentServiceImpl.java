package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.model.CourseOffering;
import io.github.dafnipapado.university.model.Enrollment;
import io.github.dafnipapado.university.model.Student;
import io.github.dafnipapado.university.model.User;
import io.github.dafnipapado.university.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentServiceImpl implements IEnrollmentService {

    private final IUtilityService utilityService;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public void enroll(UUID courseOfferingUuid) throws EntityNotFoundException, EntityAlreadyExistsException {
        try {
            //get logged-in student
            Student student = utilityService.getLoggedInStudent();
            //get course offering
            CourseOffering courseOffering = utilityService.getCourseOfferingByUuidAndDeletedFalse(courseOfferingUuid);

            //check for existing enrollment & save
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setOffering(courseOffering);
            if (utilityService.isEnrollmentExists(enrollment)) {
                throw new EntityAlreadyExistsException("Student with uuid = " + student.getUuid() + " is already enrolled in " +
                        "course offering with uuid = " + courseOffering.getUuid());
            }
            enrollmentRepository.save(enrollment);
            courseOffering.addEnrollment(enrollment);

            log.info("Student with uuid = {} was enrolled in course offering with uuid = {} successfully.", student.getUuid(), courseOffering.getUuid());

        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Student enrollment in course offering with uuid = {} failed.", courseOfferingUuid);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void withdraw(UUID courseOfferingUuid) throws EntityNotFoundException {
        try {
            User user = utilityService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user.getStudent() == null) {
                throw new EntityNotFoundException("Student with username = " + user.getUsername() + " not found.");
            }
            Student student = user.getStudent();
            CourseOffering courseOffering = utilityService.getCourseOfferingByUuidAndDeletedFalse(courseOfferingUuid);
            Enrollment enrollment = utilityService.getByStudentIdCourseOfferingId(student.getId(), courseOffering.getId());

            enrollmentRepository.delete(enrollment);
            courseOffering.removeEnrollment(enrollment);

            log.info("Student with uuid = {} withdrew from course offering with uuid = {}.", student.getUuid(), courseOffering.getUuid());

        } catch (EntityNotFoundException e) {
            log.error("Withdrawal of student from course offering with uuid = {} failed.", courseOfferingUuid);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public void enrollByAdmin(UUID courseOfferingUuid, String studentAM) throws EntityNotFoundException, EntityAlreadyExistsException {
        try {
            CourseOffering courseOffering = utilityService.getCourseOfferingByUuidAndDeletedFalse(courseOfferingUuid);
            Student student = utilityService.getStudentByStudentAmAndDeletedFalse(studentAM);

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setOffering(courseOffering);
            if (utilityService.isEnrollmentExists(enrollment)) {
                throw new EntityAlreadyExistsException("Student with AM = " + student.getStudentAM() + " is already enrolled in " +
                        "course offering with uuid = " + courseOffering.getUuid());
            }
            enrollmentRepository.save(enrollment);
            courseOffering.addEnrollment(enrollment);

            log.info("Student with AM = {} was enrolled in course offering with uuid = {} successfully.", studentAM, courseOfferingUuid);
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Failed to enroll student with AM = {} in course offering with uuid = {}.", studentAM, courseOfferingUuid);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void withdrawByAdmin(UUID courseOfferingUuid, String studentAM) throws EntityNotFoundException {
        try {
            CourseOffering courseOffering = utilityService.getCourseOfferingByUuidAndDeletedFalse(courseOfferingUuid);
            Student student = utilityService.getStudentByStudentAmAndDeletedFalse(studentAM);
            Enrollment enrollment = utilityService.getByStudentIdCourseOfferingId(student.getId(), courseOffering.getId());

            enrollmentRepository.delete(enrollment);
            courseOffering.removeEnrollment(enrollment);

            log.info("Student with uuid = {} has been withdrawn from course offering with uuid = {}.", studentAM, courseOfferingUuid);

        } catch (EntityNotFoundException e) {
            log.error("Withdrawal of student from course offering with uuid = {} failed.", courseOfferingUuid);
        }
    }
}
