package com.myapp.university.repository;

import com.myapp.university.model.static_data.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findAllByOrderByNameAsc();
}
