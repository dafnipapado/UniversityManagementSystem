package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.RegionReadOnlyDTO;

import java.util.List;

public interface IRegionService {
    List<RegionReadOnlyDTO> getAllRegions();
}
