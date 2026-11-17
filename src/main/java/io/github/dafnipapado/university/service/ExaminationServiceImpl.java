package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.examination.ExaminationInsertDTO;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.model.CourseOffering;
import io.github.dafnipapado.university.model.Examination;
import io.github.dafnipapado.university.repository.ExaminationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExaminationServiceImpl implements IExaminationService {

    private final ExaminationRepository examinationRepository;
    private final IUtilityService utilityService;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    @PreAuthorize("hasAuthority('INSERT_EXAMINATION')")
    public void saveExamination(ExaminationInsertDTO examinationInsertDTO) throws EntityNotFoundException {
        try {
            Examination examination = mapper.mapToExaminationEntity(examinationInsertDTO);
            CourseOffering courseOffering = utilityService.getCourseOfferingByUuidAndDeletedFalse(examinationInsertDTO.courseOfferingUuid());
            examination.addCourseOffering(courseOffering);
            examinationRepository.save(examination);
        } catch (EntityNotFoundException e) {
            log.error("Failed to save examination.");
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_EXAMINATION')")
    public Map<UUID, LocalDate> getExamDatesByOffering() {
        Map<UUID, LocalDate> examDatesByOffering = new HashMap<>();
        examinationRepository.findAll().forEach(e -> examDatesByOffering.put(e.getCourseOffering().getUuid(), e.getExaminationDate()));
        return examDatesByOffering;
    }
}
