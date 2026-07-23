package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.course.CourseEditDTO;
import io.github.dafnipapado.university.dto.course.CourseInsertDTO;
import io.github.dafnipapado.university.dto.course.CourseReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.model.Course;
import io.github.dafnipapado.university.model.static_data.Department;
import io.github.dafnipapado.university.repository.CourseRepository;
import io.github.dafnipapado.university.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements ICourseService{

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final Mapper mapper;

    @Override
    @PreAuthorize("hasAuthority('INSERT_COURSE')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public CourseReadOnlyDTO saveCourse(CourseInsertDTO courseInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException {
        try {
            if (courseInsertDTO.code() != null && courseRepository.findByCode(courseInsertDTO.code()).isPresent()) {
                throw new EntityAlreadyExistsException("Course with code = " + courseInsertDTO.code() + " already exists.");
            }

            Course course = mapper.mapToCourseEntity(courseInsertDTO);
            Department department = departmentRepository.findById(courseInsertDTO.departmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department with id = " + courseInsertDTO.departmentId() + " not found."));
            course.setDepartment(department);

            courseRepository.save(course);

            log.info("Course with uuid = {} was saved successfully.", course.getUuid());
            return mapper.mapToCourseReadOnlyDTO(course);

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error("Failed to save course with code =  {}.", courseInsertDTO.code());
            throw e;
        }
    }

    @Override
    public CourseEditDTO getCourseEditDTO(UUID uuid) throws EntityNotFoundException {
        Course course = courseRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Course with uuid = " + uuid + " not found."));
        return mapper.mapToCourseEditDTO(course);
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_COURSE')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public CourseReadOnlyDTO updateCourse(CourseEditDTO courseEditDTO) throws EntityNotFoundException, EntityAlreadyExistsException {
        try {
            Course course = courseRepository.findByUuidAndDeletedFalse(courseEditDTO.uuid())
                    .orElseThrow(() -> new EntityNotFoundException("Course with uuid = " + courseEditDTO.uuid() + " not found.}"));

            if (!Objects.equals(courseEditDTO.code(), course.getCode()) && courseRepository.findByCode(courseEditDTO.code()).isPresent()) {
                throw new EntityAlreadyExistsException("Course with code " + courseEditDTO.code() + " already exists.");
            }
            course.setCode(courseEditDTO.code());
            course.setName(courseEditDTO.name());
            course.setDescription(courseEditDTO.description());
            course.setEcts(courseEditDTO.ects());

            //get new department, if changed
            if (!Objects.equals(courseEditDTO.departmentId(), course.getDepartment().getId())) {
                Department updatedDepartment = departmentRepository.findById(courseEditDTO.departmentId())
                        .orElseThrow(() -> new EntityNotFoundException("Department with id = " + courseEditDTO.departmentId() + " was not found"));
                course.getDepartment().removeCourse(course);
                updatedDepartment.addCourse(course);
            }

            log.info("Course with uuid = {} was updated successfully.", course.getUuid());
            return mapper.mapToCourseReadOnlyDTO(course);

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error("Failed to update course with uuid = {}.", courseEditDTO.uuid());
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('DELETE_COURSE')")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteCourse(UUID uuid) throws EntityNotFoundException {
        try{
            Course course = courseRepository.findByUuidAndDeletedFalse(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("Course with uuid = " + uuid + " was not found."));
            course.softDelete();
            log.info("Course with uuid = {} was soft deleted successfully.", course.getUuid());
        } catch (EntityNotFoundException e) {
            log.error("Failed to soft delete course with uuid = {}.", uuid);
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<CourseReadOnlyDTO> getCoursesPaginated(Pageable pageable) {
        Page<Course> coursePage = courseRepository.findAll(pageable);
        log.info("Paginated courses fetched successfully with page = {} and size = {}", coursePage.getNumber(), coursePage.getSize());
        return coursePage.map(mapper::mapToCourseReadOnlyDTO);
    }

    @Override
    public List<CourseReadOnlyDTO> getAllCourses() {
        return courseRepository.findAllByOrderByDepartment_NameAscCodeAsc()
                .stream()
                .map(mapper::mapToCourseReadOnlyDTO)
                .toList();
    }
}
