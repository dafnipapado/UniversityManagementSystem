package com.myapp.university.repository;

import com.myapp.university.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findById(int id);
    Optional<Teacher> findByTeacherAM(String teacherAM);
    Optional<Teacher> findByUuid(UUID uuid);

}
