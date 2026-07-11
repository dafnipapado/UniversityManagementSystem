package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.user.UserInsertDTO;
import io.github.dafnipapado.university.dto.user.UserReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;

public interface IUserService {
    UserReadOnlyDTO save(UserInsertDTO userInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
}
