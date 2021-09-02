package com.learning.student.studentservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.integration.queue.ValidationServiceSender;
import com.learning.student.studentservice.persistance.model.StudentDetailsEntity;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.persistance.repository.StudentRepository;
import com.learning.student.studentservice.service.StudentService;
import com.learning.student.studentservice.util.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ValidationServiceSender validationServiceSender;
    private ObjectMapper objectMapper = new ObjectMapper();

    public StudentServiceImpl(final StudentRepository studentRepository, ValidationServiceSender validationServiceSender) {
        this.studentRepository = studentRepository;
        this.validationServiceSender = validationServiceSender;
    }

    @Override
    public Student getById(String id) {
        Optional<StudentDetailsEntity> studentDetailsEntity = studentRepository.findById(UUID.fromString(id));
        if (studentDetailsEntity.isPresent()) {
            return addMetadataToStudent(studentDetailsEntity.get());
        } else {
            throw new NoSuchElementException("No student found with the given id.");
        }
    }

    @Override
    public List<Student> getAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<StudentDetailsEntity> pagedResult = studentRepository.findAll(pageable);
        List<StudentDetailsEntity> studentDetails = pagedResult.toList();
        return studentDetails.stream()
                .map(this::addMetadataToStudent)
                .collect(Collectors.toList());
    }

    @Override
    public Student create(StudentEntity studentEntity) {
        StudentDetailsEntity studentDetailsEntity = new StudentDetailsEntity();
        studentDetailsEntity.setStudentJson(StudentMapper.convertStudentEntityToJson(studentEntity));
        studentDetailsEntity.setValid(false);
        //save the student
        StudentDetailsEntity savedDetails = studentRepository.save(studentDetailsEntity);
        //add id and valid fields
        Student savedStudent = addMetadataToStudent(savedDetails);
        //send to validation
        validationServiceSender.validate(savedStudent);
        return savedStudent;
    }

    @Override
    public void update(String id, StudentEntity studentEntity) {
        Optional<StudentDetailsEntity> studentDetails = studentRepository.findById(UUID.fromString(id));
        if (studentDetails.isPresent()) {
            StudentDetailsEntity existingStudent = studentDetails.get();
            String newStudent = StudentMapper.convertStudentEntityToJson(studentEntity);
            existingStudent.setStudentJson(newStudent);
            studentRepository.save(existingStudent);
        } else {
            throw new NoSuchElementException("No student found to update.");
        }
    }

    @Override
    public void delete(String id) {
        Optional<StudentDetailsEntity> studentDetails = studentRepository.findById(UUID.fromString(id));
        if (studentDetails.isPresent()) {
            studentRepository.delete(studentDetails.get());
        } else {
            throw new NoSuchElementException("No student found with the given id.");
        }
    }

    public void updateIsValidFlag(String id, boolean flag) {
        Optional<StudentDetailsEntity> studentDetails = studentRepository.findById(UUID.fromString(id));
        if (studentDetails.isPresent()) {
            StudentDetailsEntity studentDetailsEntity = studentDetails.get();
            studentDetailsEntity.setValid(flag);
            studentRepository.save(studentDetailsEntity);
        } else {
            // else simply don't update anything
            log.info("updateIsValidFlag: student not found. Update not performed.");
        }
    }

    private Student addMetadataToStudent(StudentDetailsEntity studentDetailsEntity) {
        Student student = StudentMapper.convertJsonToStudent(studentDetailsEntity);
        student.setId(studentDetailsEntity.getId().toString());
        student.setValid(studentDetailsEntity.isValid());
        return student;
    }
}
