package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.RoleReadOnlyDTO;

import java.util.List;

public interface IRoleService {
    List<RoleReadOnlyDTO> getAllRoles();
}
