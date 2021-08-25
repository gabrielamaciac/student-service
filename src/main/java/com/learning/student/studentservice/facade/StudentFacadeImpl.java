package com.learning.student.studentservice.facade;

import com.learning.student.studentservice.persistance.model.Student;
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
    public Student create(Student student) {
        return studentService.create(student);
    }

    @Override
    public Student getById(String id) {
        return studentService.getById(id);
    }

    @Override
    public List<Student> getAll() {
        return studentService.getAll();
    }

    @Override
    public void update(String id, Student student) {
        studentService.update(id, student);
    }

    @Override
    public void delete(String id) {
        studentService.delete(id);
    }
}
