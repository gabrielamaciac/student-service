package com.learning.student.studentservice.persistance.repository;

import com.learning.student.studentservice.persistance.model.StudentDetailsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends PagingAndSortingRepository<StudentDetailsEntity, UUID> {
    @Query(value = "SELECT * FROM student WHERE student_json->>'cnp' = :cnp", nativeQuery = true)
    List<StudentDetailsEntity> findByCnp(String cnp);
}
