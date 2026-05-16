package com.myapp.university.service;

import com.myapp.university.dto.TeacherInsertDTO;
import com.myapp.university.dto.TeacherReadOnlyDTO;
import com.myapp.university.exception.UserAlreadyExistsException;
import com.myapp.university.model.Teacher;
import com.myapp.university.repository.TeacherRepository;


public interface ITeacherService {

    TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO) throws UserAlreadyExistsException;
}
