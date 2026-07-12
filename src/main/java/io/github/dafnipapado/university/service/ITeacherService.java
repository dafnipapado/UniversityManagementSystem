package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.teacher.TeacherEditDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherInsertDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface ITeacherService {

    TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    TeacherEditDTO getTeacherEditDTO(UUID uuid) throws EntityNotFoundException;
    TeacherReadOnlyDTO updateTeacher(TeacherEditDTO teacherEditDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    void deleteTeacher(UUID uuid) throws EntityNotFoundException;
    TeacherReadOnlyDTO getIndex() throws EntityNotFoundException;
    User getUserByUsername(String username) throws EntityNotFoundException;
    Page<TeacherReadOnlyDTO> getTeachersPaginated(Pageable pageable);


}
