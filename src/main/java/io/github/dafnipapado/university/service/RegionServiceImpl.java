package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.RegionReadOnlyDTO;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionServiceImpl implements IRegionService {

    private final RegionRepository regionRepository;
    private final Mapper mapper;

    @Override
    public List<RegionReadOnlyDTO> getAllRegions() {

        return regionRepository.findAllByOrderByNameAsc()
                .stream()
                .map(mapper::mapToRegionReadOnlyDTO)
                .toList();
    }
}
