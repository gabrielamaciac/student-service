package com.learning.student.studentservice.facade;

import com.learning.student.studentservice.persistance.model.Student;

import java.util.List;

public interface StudentFacade {
    Student create(Student student);

    Student getById(String id);

    List<Student> getAll();

    void update(String id, Student student);

    void delete(String id);
}
