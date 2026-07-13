package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.DepartmentReadOnlyDTO;

import java.util.List;

public interface IDepartmentService {
    List<DepartmentReadOnlyDTO> getAllDepartments();

}
