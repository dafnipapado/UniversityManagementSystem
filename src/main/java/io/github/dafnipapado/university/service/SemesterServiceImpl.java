package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.semester.SemesterInsertDTO;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.model.Semester;
import io.github.dafnipapado.university.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SemesterServiceImpl implements ISemesterService{

    private final Mapper mapper;
    private final SemesterRepository semesterRepository;

    @Override
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
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAllByOrderByYearAscNameAsc()
                .stream()
                .toList();
    }
}
