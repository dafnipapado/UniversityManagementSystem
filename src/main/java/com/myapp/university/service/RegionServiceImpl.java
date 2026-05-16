package com.myapp.university.service;

import com.myapp.university.dto.RegionReadOnlyDTO;
import com.myapp.university.mapper.Mapper;
import com.myapp.university.model.static_data.Region;
import com.myapp.university.repository.RegionRepository;
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
