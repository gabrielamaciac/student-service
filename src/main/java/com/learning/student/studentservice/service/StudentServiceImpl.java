package com.learning.student.studentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.studentservice.facade.StudentService;
import com.learning.student.studentservice.persistance.model.Student;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.persistance.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public String getById(String id) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(UUID.fromString(id));
        if (studentEntity.isPresent()) {
            return studentEntity.get().getStudentJson();
        } else {
            throw new NoSuchElementException("No student found with the given id.");
        }
    }

    @Override
    public List<String> getAll() {
        List<StudentEntity> studentEntities = studentRepository.findAll();
        return studentEntities.size() > 0 ?
                studentEntities.stream().map(StudentEntity::getStudentJson).collect(Collectors.toList())
                : Collections.emptyList();
    }

    @Override
    public String create(Student student) {
        StudentEntity studentEntity = new StudentEntity();
        try {
            studentEntity.setStudentJson(objectMapper.writeValueAsString(student));
            //TODO isValid field temporarily hardcoded
            studentEntity.setValid(true);
            studentRepository.save(studentEntity);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
        return studentEntity.getStudentJson();
    }

    @Override
    public String update(Student student) {
        return null;
    }

}
