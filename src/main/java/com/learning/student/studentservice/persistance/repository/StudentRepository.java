package com.learning.student.studentservice.persistance.repository;

import com.learning.student.studentservice.persistance.model.StudentDetailsEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface StudentRepository extends PagingAndSortingRepository<StudentDetailsEntity, UUID> {
}
