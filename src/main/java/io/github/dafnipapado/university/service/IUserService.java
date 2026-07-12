package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.user.UserInsertDTO;
import io.github.dafnipapado.university.dto.user.UserReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.model.User;

public interface IUserService {
    UserReadOnlyDTO save(UserInsertDTO userInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
    User getUserByUsername(String username) throws EntityNotFoundException;
}
