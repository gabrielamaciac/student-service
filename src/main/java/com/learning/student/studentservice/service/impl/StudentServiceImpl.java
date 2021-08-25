package com.learning.student.studentservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.studentservice.persistance.model.Student;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.persistance.repository.StudentRepository;
import com.learning.student.studentservice.service.StudentService;
import com.learning.student.studentservice.service.util.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    public StudentServiceImpl(final StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getById(String id) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(UUID.fromString(id));
        if (studentEntity.isPresent()) {
            return addMetadataToStudent(studentEntity.get());
        } else {
            throw new NoSuchElementException("No student found with the given id.");
        }
    }

    private Student addMetadataToStudent(StudentEntity studentEntity) {
        Student student = StudentMapper.convertJsonToStudent(studentEntity);
        student.setId(studentEntity.getId().toString());
        student.setValid(studentEntity.isValid());
        return student;
    }

    @Override
    public List<Student> getAll() {
        List<StudentEntity> studentEntities = studentRepository.findAll();
        return studentEntities.stream()
                .map(this::addMetadataToStudent)
                .collect(Collectors.toList());
    }

    @Override
    public Student create(Student student) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setStudentJson(StudentMapper.convertStudentToJson(student));
        studentEntity.setValid(false);
        StudentEntity savedStudent = studentRepository.save(studentEntity);
        return addMetadataToStudent(savedStudent);
    }

    //TODO use the same method
    @Override
    public void update(String id, Student student) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(UUID.fromString(id));
        if (studentEntity.isPresent()) {
            StudentEntity existingStudent = studentEntity.get();
            String newStudent = StudentMapper.convertStudentToJson(student);
            existingStudent.setStudentJson(newStudent);
            studentRepository.save(existingStudent);
        } else {
            throw new NoSuchElementException("No student found to update.");
        }
    }

    @Override
    public void delete(String id) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(UUID.fromString(id));
        if (studentEntity.isPresent()) {
            studentRepository.delete(studentEntity.get());
        } else {
            throw new NoSuchElementException("No student found with the given id.");
        }
    }

}
