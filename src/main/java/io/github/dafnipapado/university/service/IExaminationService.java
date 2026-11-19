package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.examination.ExaminationEditDTO;
import io.github.dafnipapado.university.dto.examination.ExaminationInsertDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityInvalidArgumentException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public interface IExaminationService {
    void saveExamination(ExaminationInsertDTO examinationInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;
    ExaminationEditDTO getExaminationEditDTO(UUID courseOfferingUuid) throws EntityNotFoundException;
    void updateExamination(ExaminationEditDTO examinationEditDTO) throws EntityNotFoundException, EntityInvalidArgumentException ;
    Map<UUID, LocalDate> getExamDatesByOffering();
}
