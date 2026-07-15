package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.semester.SemesterInsertDTO;
import io.github.dafnipapado.university.model.Semester;

import java.util.List;

public interface ISemesterService {
    void saveSemester(SemesterInsertDTO semesterInsertDTO);
    List<Semester> getAllSemesters();
}
