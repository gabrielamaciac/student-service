package com.learning.student.studentservice.persistance.repository;

import com.learning.student.studentservice.persistance.model.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {
}
