package com.myapp.university.service;

import com.myapp.university.dto.RoleReadOnlyDTO;

import java.util.List;

public interface IRoleService {
    List<RoleReadOnlyDTO> getAllRoles();
}
