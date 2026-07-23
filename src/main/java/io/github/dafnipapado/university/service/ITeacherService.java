package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.teacher.TeacherEditDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherInsertDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface ITeacherService {

    TeacherReadOnlyDTO getIndex() throws EntityNotFoundException;
    TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    TeacherEditDTO getTeacherEditDTO(UUID uuid) throws EntityNotFoundException;
    TeacherReadOnlyDTO updateTeacher(TeacherEditDTO teacherEditDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    void deleteTeacher(UUID uuid) throws EntityNotFoundException;
    Page<TeacherReadOnlyDTO> getTeachersPaginated(Pageable pageable);
    List<TeacherReadOnlyDTO> getAllTeachers();
}
