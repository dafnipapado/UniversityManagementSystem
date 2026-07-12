package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.user.UserInsertDTO;
import io.github.dafnipapado.university.dto.user.UserReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.model.User;
import io.github.dafnipapado.university.model.static_data.Role;
import io.github.dafnipapado.university.repository.RoleRepository;
import io.github.dafnipapado.university.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public UserReadOnlyDTO save(UserInsertDTO userInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException {
        try{
            //check if username already exists
            if (userInsertDTO.username() != null && userRepository.findByUsername(userInsertDTO.username()).isPresent()) {
                throw new EntityAlreadyExistsException("User with username = " + userInsertDTO.username() + " already exists.");
            }

            User user = mapper.mapToUserEntity(userInsertDTO);
            user.setPassword(passwordEncoder.encode(userInsertDTO.password()));

            //set role to user
            Role role = roleRepository.findById(userInsertDTO.roleId())
                    .orElseThrow(() -> new EntityNotFoundException("Role with id = " + userInsertDTO.roleId() + " was not found."));
            role.addUser(user);

            userRepository.save(user);
            return mapper.mapToUserReadOnlyDTO(user);

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error("Failed to save user with username = {}", userInsertDTO.username());
            throw e;
        }
    }

    @Override
    public User getUserByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username = " + username + " not found."));
    }
}
