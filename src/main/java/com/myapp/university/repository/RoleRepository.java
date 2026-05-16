package com.myapp.university.repository;

import com.myapp.university.model.static_data.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
