package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.UserInsertDTO;
import io.github.dafnipapado.university.dto.UserReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;

public interface IUserService {
    UserReadOnlyDTO save(UserInsertDTO userInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
}
