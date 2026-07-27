package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.course_offering.CourseOfferingEditDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingInsertDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.model.*;
import io.github.dafnipapado.university.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseOfferingServiceImpl implements ICourseOfferingService{

    private final CourseOfferingRepository courseOfferingRepository;
    private final SemesterRepository semesterRepository;
    private final IUtilityService utilityService;
    private final Mapper mapper;

    @Override
    @PreAuthorize("hasAuthority('INSERT_COURSE_OFFERING')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public CourseOfferingReadOnlyDTO saveCourseOffering(CourseOfferingInsertDTO courseOfferingInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException {
        try {
            CourseOffering courseOffering = new CourseOffering();

            Course course = utilityService.getCourseByUuid(courseOfferingInsertDTO.courseUuid());
            Teacher teacher = utilityService.getTeacherByUuid(courseOfferingInsertDTO.teacherUuid());
            Semester semester = semesterRepository.findById(courseOfferingInsertDTO.semesterId())
                    .orElseThrow(() -> new EntityNotFoundException("Semester with id = " + courseOfferingInsertDTO.semesterId() + " not found."));

            if(courseOfferingRepository.existsByCourseUuidAndTeacherUuidAndSemesterId(
                    courseOfferingInsertDTO.courseUuid(), courseOfferingInsertDTO.teacherUuid(), courseOfferingInsertDTO.semesterId())) {
                throw new EntityAlreadyExistsException("Course offering already exists.");
            }

            course.addCourseOffering(courseOffering);
            teacher.addCourseOffering(courseOffering);
            semester.addCourseOffering(courseOffering);
            courseOfferingRepository.save(courseOffering);

            log.info("Course offering was saved successfully.");
            return mapper.mapToCourseOfferingReadOnlyDTO(courseOffering, false);
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Failed to save course offering.");
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_COURSE_OFFERING')")
    public CourseOfferingEditDTO getCourseOfferingEditDTO(UUID uuid) throws EntityNotFoundException {
        CourseOffering courseOffering = utilityService.getCourseOfferingByUuidAndDeletedFalse(uuid);
        return mapper.mapToCourseOfferingEditDTO(courseOffering);
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_COURSE_OFFERING')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public CourseOfferingReadOnlyDTO updateCourseOffering(CourseOfferingEditDTO courseOfferingEditDTO) throws EntityNotFoundException, EntityAlreadyExistsException {
        try {
            CourseOffering courseOffering = utilityService.getCourseOfferingByUuidAndDeletedFalse(courseOfferingEditDTO.uuid());

            boolean isCourseUpdated = !Objects.equals(courseOfferingEditDTO.courseUuid(), courseOffering.getCourse().getUuid());
            boolean isTeacherUpdated = !Objects.equals(courseOfferingEditDTO.teacherUuid(), courseOffering.getTeacher().getUuid());
            boolean isSemesterUpdated = !Objects.equals(courseOfferingEditDTO.semesterId(), courseOffering.getSemester().getId());

            if ((isCourseUpdated || isTeacherUpdated || isSemesterUpdated) && courseOfferingRepository.existsByCourseUuidAndTeacherUuidAndSemesterId(
                    courseOfferingEditDTO.courseUuid(), courseOfferingEditDTO.teacherUuid(), courseOfferingEditDTO.semesterId())) {
                throw new EntityAlreadyExistsException("Course offering already exists.");
            }

            Course course = utilityService.getCourseByUuid(courseOfferingEditDTO.courseUuid());
            Teacher teacher = utilityService.getTeacherByUuid(courseOfferingEditDTO.teacherUuid());
            Semester semester = semesterRepository.findById(courseOfferingEditDTO.semesterId())
                    .orElseThrow(() -> new EntityNotFoundException("Semester with id = " + courseOfferingEditDTO.semesterId() + " not found."));

            if (isCourseUpdated) {
                courseOffering.getCourse().removeCourseOffering(courseOffering);
                course.addCourseOffering(courseOffering);
            }
            if (isTeacherUpdated) {
                courseOffering.getTeacher().removeCourseOffering(courseOffering);
                teacher.addCourseOffering(courseOffering);
            }
            if (isSemesterUpdated) {
                courseOffering.getSemester().removeCourseOffering(courseOffering);
                semester.addCourseOffering(courseOffering);
            }

            log.info("Course offering was updated successfully.");
            return mapper.mapToCourseOfferingReadOnlyDTO(courseOffering, false);
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Failed to update course offering.");
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('DELETE_COURSE_OFFERING')")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteCourseOffering(UUID uuid) throws EntityNotFoundException {
        try {
            CourseOffering courseOffering = utilityService.getCourseOfferingByUuidAndDeletedFalse(uuid);
            courseOffering.softDelete();
        } catch (EntityNotFoundException e) {
            log.error("Failed to soft delete course offering with uuid = {}.", uuid);
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<CourseOfferingReadOnlyDTO> getCourseOfferingsPaginated(Pageable pageable) {
        Page<CourseOffering> courseOfferingPage = courseOfferingRepository.findAll(pageable);
        log.info("Paginated course offerings fetched successfully with page = {} and size = {}", courseOfferingPage.getNumber(), courseOfferingPage.getSize());
        return courseOfferingPage.map(courseOffering -> mapper.mapToCourseOfferingReadOnlyDTO(courseOffering, false));
    }

    @Override
    public Page<CourseOfferingReadOnlyDTO> getCourseOfferingsPaginatedDeletedFalse(Pageable pageable) throws EntityAlreadyExistsException, EntityNotFoundException {
        Page<CourseOffering> courseOfferingPage = courseOfferingRepository.findAllByDeletedFalse(pageable);
        //get all courseOfferings logged-in student is enrolled into
        Set<CourseOffering> studentCourseOfferings = getCourseOfferingsByStudent();
        Map<CourseOffering, Boolean> courseOfferingsMap = new HashMap<>();
        courseOfferingPage.forEach(courseOffering -> courseOfferingsMap.put(courseOffering, studentCourseOfferings.contains(courseOffering)));
        return courseOfferingPage.map(courseOffering -> mapper.mapToCourseOfferingReadOnlyDTO(courseOffering, courseOfferingsMap.get(courseOffering)));
    }

    @Override
    public boolean isStudentEnrolled(CourseOffering courseOffering) throws EntityNotFoundException, EntityAlreadyExistsException {
        Student student = utilityService.getLoggedInStudent();
        Enrollment enrollment = utilityService.getByStudentIdCourseOfferingId(student.getId(), courseOffering.getId());
        return utilityService.isEnrollmentExists(enrollment);
    }

    @Override
    public Set<CourseOffering> getCourseOfferingsByStudent() throws EntityNotFoundException {
        Set<CourseOffering> courseOfferings = new HashSet<>();
        Student student = utilityService.getLoggedInStudent();
        student.getEnrollments().forEach(enrollment -> courseOfferings.add(enrollment.getOffering()));
        return courseOfferings;
    }
}
