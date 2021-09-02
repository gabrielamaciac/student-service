package com.learning.student.studentservice.facade;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.service.StudentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentFacadeImpl implements StudentFacade {
    private StudentService studentService;

    public StudentFacadeImpl(final StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Student create(StudentEntity studentEntity) {
        return studentService.create(studentEntity);
    }

    @Override
    public Student getById(String id) {
        return studentService.getById(id);
    }

    @Override
    public List<Student> getAll(int pageNo, int pageSize) {
        return studentService.getAll(pageNo, pageSize);
    }

    @Override
    public void update(String id, StudentEntity studentEntity) {
        studentService.update(id, studentEntity);
    }

    @Override
    public void delete(String id) {
        studentService.delete(id);
    }
}
