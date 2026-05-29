package com.myapp.university.repository;

import com.myapp.university.model.static_data.Capability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapabilityRepository extends JpaRepository<Capability, Long> {
}
