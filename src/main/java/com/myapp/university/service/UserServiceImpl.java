package com.myapp.university.service;

import com.myapp.university.dto.UserInsertDTO;
import com.myapp.university.dto.UserReadOnlyDTO;
import com.myapp.university.exception.EntityAlreadyExistsException;
import com.myapp.university.exception.EntityNotFoundException;
import com.myapp.university.mapper.Mapper;
import com.myapp.university.model.User;
import com.myapp.university.model.static_data.Role;
import com.myapp.university.repository.RoleRepository;
import com.myapp.university.repository.UserRepository;
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
                throw new EntityAlreadyExistsException("User with username = {userInsertDTO.username()} already exists.");
            }

            User user = mapper.mapToUserEntity(userInsertDTO);
            user.setPassword(passwordEncoder.encode(userInsertDTO.password()));

            //set role to user
            Role role = roleRepository.findById(userInsertDTO.roleId())
                    .orElseThrow(() -> new EntityNotFoundException("No role with id = {userInsertDTO.roleId()} was found."));
            role.addUser(user);

            userRepository.save(user);

            return mapper.mapToUserReadOnlyDTO(user);

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
