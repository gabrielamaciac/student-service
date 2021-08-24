package com.learning.student.studentservice.facade;

import com.learning.student.studentservice.persistance.model.Student;

import java.util.List;

public interface StudentService {
    String getById(String id);

    List<String> getAll();

    String create(Student student);

    String update(Student student);
}
