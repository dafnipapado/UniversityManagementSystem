package com.myapp.university.service;

import com.myapp.university.dto.TeacherEditDTO;
import com.myapp.university.dto.TeacherInsertDTO;
import com.myapp.university.dto.TeacherReadOnlyDTO;
import com.myapp.university.exception.EntityAlreadyExistsException;
import com.myapp.university.exception.EntityNotFoundException;
import com.myapp.university.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


public interface ITeacherService {

    TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    TeacherEditDTO findTeacherByUuid(UUID uuid) throws EntityNotFoundException;
    TeacherReadOnlyDTO updateTeacher(TeacherEditDTO teacherEditDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    void deleteTeacher(UUID uuid);
    TeacherReadOnlyDTO getIndex() throws EntityNotFoundException;
    User getUserByUsername(String username) throws EntityNotFoundException;
    Page<TeacherReadOnlyDTO> getTeachersPaginated(Pageable pageable);


}
