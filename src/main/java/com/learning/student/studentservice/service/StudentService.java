package com.learning.student.studentservice.service;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.persistance.model.StudentEntity;

import java.util.List;

public interface StudentService {
    Student getById(String id);

    List<Student> getAll(int pageNo, int pageSize);

    Student create(StudentEntity studentEntity);

    void update(String id, StudentEntity studentEntity);

    void delete(String id);

    void updateIsValidFlag(String id, boolean flag);
}
