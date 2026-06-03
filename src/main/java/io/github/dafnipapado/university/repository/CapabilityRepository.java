package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.static_data.Capability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapabilityRepository extends JpaRepository<Capability, Long> {
}
