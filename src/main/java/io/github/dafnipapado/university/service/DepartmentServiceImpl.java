package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.DepartmentReadOnlyDTO;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements IDepartmentService{

    private final DepartmentRepository departmentRepository;
    private final Mapper mapper;

    @Override
    public List<DepartmentReadOnlyDTO> getAllDepartments() {
        return departmentRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(mapper::mapToDepartmentReadOnlyDTO)
                .toList();
    }
}
