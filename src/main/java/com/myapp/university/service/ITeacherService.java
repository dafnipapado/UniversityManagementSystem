package com.myapp.university.service;

import com.myapp.university.dto.TeacherInsertDTO;
import com.myapp.university.dto.TeacherReadOnlyDTO;
import com.myapp.university.exception.UserAlreadyExistsException;

import java.util.List;


public interface ITeacherService {

    TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO) throws UserAlreadyExistsException;
    List<TeacherReadOnlyDTO> viewTeachers();
}
