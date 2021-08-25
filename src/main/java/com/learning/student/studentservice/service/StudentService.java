package com.learning.student.studentservice.service;

import com.learning.student.studentservice.persistance.model.Student;

import java.util.List;

public interface StudentService {
    Student getById(String id);

    List<Student> getAll();

    Student create(Student student);

    void update(String id, Student student);

    void delete(String id);
}
