package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.course_offering.CourseOfferingEditDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingInsertDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.model.CourseOffering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICourseOfferingService {
    CourseOfferingReadOnlyDTO saveCourseOffering(CourseOfferingInsertDTO courseOfferingInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    CourseOfferingEditDTO getCourseOfferingEditDTO(UUID uuid) throws EntityNotFoundException;
    CourseOfferingReadOnlyDTO updateCourseOffering(CourseOfferingEditDTO courseOfferingEditDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    void deleteCourseOffering(UUID uuid) throws EntityNotFoundException;
    Page<CourseOfferingReadOnlyDTO> getCourseOfferingsPaginated(Pageable pageable);
    CourseOffering getCourseOfferingByUuid(UUID uuid) throws EntityNotFoundException;
}
