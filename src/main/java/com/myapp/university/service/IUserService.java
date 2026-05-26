package com.myapp.university.service;

import com.myapp.university.dto.UserInsertDTO;
import com.myapp.university.dto.UserReadOnlyDTO;
import com.myapp.university.exception.EntityAlreadyExistsException;
import com.myapp.university.exception.EntityNotFoundException;

public interface IUserService {
    UserReadOnlyDTO save(UserInsertDTO userInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException;
}
