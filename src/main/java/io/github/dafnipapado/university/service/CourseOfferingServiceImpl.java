package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.course_offering.CourseOfferingEditDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingInsertDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.model.*;
import io.github.dafnipapado.university.repository.CourseOfferingRepository;
import io.github.dafnipapado.university.repository.CourseRepository;
import io.github.dafnipapado.university.repository.SemesterRepository;
import io.github.dafnipapado.university.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseOfferingServiceImpl implements ICourseOfferingService{

    private final CourseOfferingRepository courseOfferingRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final SemesterRepository semesterRepository;
    private final ICourseService courseService;
    private final ITeacherService teacherService;
    private final Mapper mapper;

    @Override
    @PreAuthorize("hasAuthority('INSERT_COURSE_OFFERING')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public CourseOfferingReadOnlyDTO saveCourseOffering(CourseOfferingInsertDTO courseOfferingInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException {
        try {
            CourseOffering courseOffering = new CourseOffering();

            Course course = courseService.getCourseByUuid(courseOfferingInsertDTO.courseUuid());
            Teacher teacher = teacherService.getTeacherByUuid(courseOfferingInsertDTO.teacherUuid());
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
            return mapper.mapToCourseOfferingReadOnlyDTO(courseOffering);
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Failed to save course offering.");
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public CourseOfferingEditDTO getCourseOfferingEditDTO(UUID uuid) throws EntityNotFoundException {
        CourseOffering courseOffering = getCourseOfferingByUuidAndDeletedFalse(uuid);
        return mapper.mapToCourseOfferingEditDTO(courseOffering);
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_COURSE_OFFERING')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public CourseOfferingReadOnlyDTO updateCourseOffering(CourseOfferingEditDTO courseOfferingEditDTO) throws EntityNotFoundException, EntityAlreadyExistsException {
        try {
            CourseOffering courseOffering = getCourseOfferingByUuidAndDeletedFalse(courseOfferingEditDTO.uuid());

            boolean isCourseUpdated = !Objects.equals(courseOfferingEditDTO.courseUuid(), courseOffering.getCourse().getUuid());
            boolean isTeacherUpdated = !Objects.equals(courseOfferingEditDTO.teacherUuid(), courseOffering.getTeacher().getUuid());
            boolean isSemesterUpdated = !Objects.equals(courseOfferingEditDTO.semesterId(), courseOffering.getSemester().getId());

            if ((isCourseUpdated || isTeacherUpdated || isSemesterUpdated) && courseOfferingRepository.existsByCourseUuidAndTeacherUuidAndSemesterId(
                    courseOfferingEditDTO.courseUuid(), courseOfferingEditDTO.teacherUuid(), courseOfferingEditDTO.semesterId())) {
                throw new EntityAlreadyExistsException("Course offering already exists.");
            }

            Course course = courseService.getCourseByUuid(courseOfferingEditDTO.courseUuid());
            Teacher teacher = teacherService.getTeacherByUuid(courseOfferingEditDTO.teacherUuid());
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
            return mapper.mapToCourseOfferingReadOnlyDTO(courseOffering);
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
            CourseOffering courseOffering = getCourseOfferingByUuidAndDeletedFalse(uuid);
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
        return courseOfferingPage.map(mapper::mapToCourseOfferingReadOnlyDTO);
    }

    @Override
    public CourseOffering getCourseOfferingByUuidAndDeletedFalse(UUID uuid) throws EntityNotFoundException {
        return courseOfferingRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Active course offering with uuid = " + uuid + " not found."));
    }
}
