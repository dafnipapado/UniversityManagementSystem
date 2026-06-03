package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.RoleReadOnlyDTO;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements IRoleService{
    private final RoleRepository roleRepository;
    private final Mapper mapper;

    @Override
    public List<RoleReadOnlyDTO> getAllRoles() {
        return roleRepository.findAllByOrderByNameAsc()
                .stream()
                .map(mapper::mapToRoleReadOnlyDTO)
                .toList();
    }
}
