package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.course.CourseEditDTO;
import io.github.dafnipapado.university.dto.course.CourseInsertDTO;
import io.github.dafnipapado.university.dto.course.CourseReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICourseService {
    CourseReadOnlyDTO saveCourse(CourseInsertDTO courseInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    CourseEditDTO getCourseEditDTO(UUID uuid) throws EntityNotFoundException;
    CourseReadOnlyDTO updateCourse(CourseEditDTO courseEditDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    void deleteCourse(UUID uuid) throws EntityNotFoundException;
    Page<CourseReadOnlyDTO> getCoursesPaginated(Pageable pageable);
    List<CourseReadOnlyDTO> getAllCourses();
}
