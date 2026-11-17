package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.examination.ExaminationInsertDTO;
import io.github.dafnipapado.university.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public interface IExaminationService {
    void saveExamination(ExaminationInsertDTO examinationInsertDTO) throws EntityNotFoundException;
    Map<UUID, LocalDate> getExamDatesByOffering();
}
