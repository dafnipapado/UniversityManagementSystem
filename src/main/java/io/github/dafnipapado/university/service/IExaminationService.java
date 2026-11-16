package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.examination.ExaminationInsertDTO;
import io.github.dafnipapado.university.exception.EntityNotFoundException;

public interface IExaminationService {
    void saveExamination(ExaminationInsertDTO examinationInsertDTO) throws EntityNotFoundException;
}
