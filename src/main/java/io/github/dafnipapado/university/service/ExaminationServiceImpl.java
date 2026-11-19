package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.examination.ExaminationEditDTO;
import io.github.dafnipapado.university.dto.examination.ExaminationInsertDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityInvalidArgumentException;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExaminationServiceImpl implements IExaminationService {

    private final ExaminationRepository examinationRepository;
    private final IUtilityService utilityService;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    @PreAuthorize("hasAuthority('INSERT_EXAMINATION')")
    public void saveExamination(ExaminationInsertDTO examinationInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            Examination examination = mapper.mapToExaminationEntity(examinationInsertDTO);
            CourseOffering courseOffering = utilityService.getCourseOfferingByUuidAndDeletedFalse(examinationInsertDTO.courseOfferingUuid());

            if (examinationRepository.findByCourseOfferingId(courseOffering.getId()).isPresent()) {
                throw new EntityAlreadyExistsException("Examination for this course offering already exists.");
            }

            //reject past date and time input
            LocalDateTime now = LocalDateTime.now();
            LocalDate date = examination.getExaminationDate();
            LocalTime time = examination.getStartTime();
            if (date.isBefore(now.toLocalDate())) {
                throw new EntityInvalidArgumentException("Examination date has passed.");
            }
            if (date.isEqual(now.toLocalDate()) && time != null && time.isBefore(now.toLocalTime())) {
                throw new EntityInvalidArgumentException("Examination time has passed.");
            }

            examination.addCourseOffering(courseOffering);
            examinationRepository.save(examination);
        } catch (EntityNotFoundException e) {
            log.error("Failed to save examination.");
            throw e;
        } catch (EntityAlreadyExistsException e) {
            log.error(e.getMessage());
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.error("Invalid examination datetime");
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_EXAMINATION')")
    public ExaminationEditDTO getExaminationEditDTO(UUID courseOfferingUuid) throws EntityNotFoundException {
        Examination examination = examinationRepository.findByCourseOfferingUuid(courseOfferingUuid)
                .orElseThrow(() -> new EntityNotFoundException("Examination was not found."));
        return mapper.mapToExaminationEditDTO(examination);
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_EXAMINATION')")
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityInvalidArgumentException.class})
    public void updateExamination(ExaminationEditDTO examinationEditDTO) throws EntityNotFoundException, EntityInvalidArgumentException {
        try {
            Examination examination = examinationRepository.findByUuid(examinationEditDTO.uuid())
                    .orElseThrow(() -> new EntityNotFoundException("Examination was not found"));

            if (!Objects.equals(examination.getExaminationDate(), examinationEditDTO.examinationDate())) {
                LocalDateTime now = LocalDateTime.now();
                LocalDate date = examinationEditDTO.examinationDate();
                LocalTime time = examinationEditDTO.startTime();
                if (date.isBefore(now.toLocalDate())) {
                    throw new EntityInvalidArgumentException("Examination date has passed.");
                }
                if (date.isEqual(now.toLocalDate()) && time != null && time.isBefore(now.toLocalTime())) {
                    throw new EntityInvalidArgumentException("Examination time has passed.");
                }
                examination.setExaminationDate(date);
                examination.setStartTime(time);
            }
            examination.setDuration(examinationEditDTO.duration());
            examination.setPassingGrade(examinationEditDTO.passingGrade());

            log.info("Examination with uuid = {} was updated successfully.", examination.getUuid());
        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            log.error("Failed to update examination with uuid = {}.", examinationEditDTO.uuid());
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
