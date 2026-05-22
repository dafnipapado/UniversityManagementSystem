package com.myapp.university.service;

import com.myapp.university.dto.TeacherEditDTO;
import com.myapp.university.dto.TeacherInsertDTO;
import com.myapp.university.dto.TeacherReadOnlyDTO;
import com.myapp.university.exception.EntityAlreadyExistsException;
import com.myapp.university.exception.EntityNotFoundException;

import java.util.List;
import java.util.UUID;


public interface ITeacherService {

    TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    TeacherEditDTO findTeacherByUuid(UUID uuid) throws EntityNotFoundException;
    TeacherReadOnlyDTO updateTeacher(TeacherEditDTO teacherEditDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    List<TeacherReadOnlyDTO> viewTeachers();


}
