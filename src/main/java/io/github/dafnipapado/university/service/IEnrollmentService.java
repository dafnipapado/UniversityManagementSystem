package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;

import java.util.UUID;

public interface IEnrollmentService {
    void enroll(UUID courseOfferingUuid) throws EntityNotFoundException, EntityAlreadyExistsException;
    void withdraw(UUID courseOfferingUuid) throws EntityNotFoundException;
    void enrollByAdmin(UUID courseOfferingUuid, String studentAM) throws EntityNotFoundException, EntityAlreadyExistsException;
    void withdrawByAdmin(UUID courseOfferingUuid, String studentAM) throws EntityNotFoundException;
}
