package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.student.StudentEditDTO;
import io.github.dafnipapado.university.dto.student.StudentInsertDTO;
import io.github.dafnipapado.university.dto.student.StudentReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IStudentService {
    StudentReadOnlyDTO getIndex() throws EntityNotFoundException;
    StudentReadOnlyDTO saveStudent(StudentInsertDTO studentInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    StudentEditDTO getStudentEditDTO(UUID uuid) throws EntityNotFoundException;
    StudentReadOnlyDTO updateStudent(StudentEditDTO studentEditDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    void deleteStudent(UUID uuid) throws EntityNotFoundException;
    Page<StudentReadOnlyDTO> getStudentsPaginated(Pageable pageable);

}
