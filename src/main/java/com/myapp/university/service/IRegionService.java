package com.myapp.university.service;

import com.myapp.university.dto.RegionReadOnlyDTO;

import java.util.List;

public interface IRegionService {
    List<RegionReadOnlyDTO> getAllRegions();
}
