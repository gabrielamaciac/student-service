package com.learning.student.studentservice.facade;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.persistance.model.StudentEntity;

import java.util.List;

public interface StudentFacade {
    Student create(StudentEntity studentEntity);

    Student getById(String id);

    List<Student> getAll(int pageNo, int pageSize);

    void update(String id, StudentEntity studentEntity);

    void delete(String id);
}
