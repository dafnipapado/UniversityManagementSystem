package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.semester.SemesterInsertDTO;
import io.github.dafnipapado.university.dto.semester.SemesterReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.model.Semester;
import io.github.dafnipapado.university.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SemesterServiceImpl implements ISemesterService{

    private final Mapper mapper;
    private final SemesterRepository semesterRepository;

    @Override
    @PreAuthorize("hasAuthority('INSERT_SEMESTER')")
    public void saveSemester(SemesterInsertDTO semesterInsertDTO) {
        try {
            Semester semester = mapper.mapToSemesterEntity(semesterInsertDTO);
            semesterRepository.save(semester);
        } catch (Exception e) {
            log.error("Failed to save semester = {} {}.", semesterInsertDTO.name(), semesterInsertDTO.year());
            throw e;
        }
    }

    @Override
    public List<SemesterReadOnlyDTO> getAllSemesters() {
        return semesterRepository.findAllByOrderByYearAscNameAsc()
                .stream()
                .map(mapper::mapToSemesterReadOnlyDTO)
                .toList();
    }

    @Override
    public Page<SemesterReadOnlyDTO> getSemestersPaginated(Pageable pageable) {
        Page<Semester> semesterPage = semesterRepository.findAll(pageable);
        log.info("Paginated students fetched successfully with page = {} and size = {}", semesterPage.getNumber(), semesterPage.getSize());
        return semesterPage.map(mapper::mapToSemesterReadOnlyDTO);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void activateSemester(UUID uuid) throws EntityNotFoundException {
        Semester semester = semesterRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Semester not found"));
        //inactivate previously active semester
        semesterRepository.findByActiveTrue()
                .ifPresent(s -> { s.setActive(false); semesterRepository.save(s); });
        //activate the new semester
        semester.setActive(true);
        log.error(String.valueOf(semester.isActive()));
    }
}
