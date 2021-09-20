package com.learning.student.studentservice.service.impl;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.integration.model.FullStudentMessage;
import com.learning.student.studentservice.integration.model.search.OperationType;
import com.learning.student.studentservice.integration.model.search.SearchPayload;
import com.learning.student.studentservice.integration.model.search.StudentSearch;
import com.learning.student.studentservice.integration.queue.SearchServiceSender;
import com.learning.student.studentservice.integration.queue.ValidationServiceSender;
import com.learning.student.studentservice.persistance.model.StudentDetailsEntity;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.persistance.repository.StudentRepository;
import com.learning.student.studentservice.service.StudentService;
import com.learning.student.studentservice.util.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ValidationServiceSender validationServiceSender;
    private final SearchServiceSender searchServiceSender;
    private final ModelMapper modelMapper;

    public StudentServiceImpl(final StudentRepository studentRepository, ValidationServiceSender validationServiceSender,
                              SearchServiceSender searchServiceSender, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.validationServiceSender = validationServiceSender;
        this.searchServiceSender = searchServiceSender;
        this.modelMapper = modelMapper;
    }

    @Override
    public Student getById(String id) {
        Optional<StudentDetailsEntity> studentDetailsEntity = studentRepository.findById(UUID.fromString(id));
        return studentDetailsEntity.map(this::addMetadataToStudent)
                .orElseThrow(() -> new NoSuchElementException("No student found with the given id."));
    }

    @Override
    public List<Student> getAll(int pageNo, int pageSize) {
        Page<StudentDetailsEntity> pagedResult = studentRepository.findAll(PageRequest.of(pageNo, pageSize));
        return pagedResult.toList().stream()
                .map(this::addMetadataToStudent)
                .collect(Collectors.toList());
    }

    @Override
    public Student create(StudentEntity studentEntity) {
        //if it exists, stop
        List<StudentDetailsEntity> studentDetails = studentRepository.findByCnp(studentEntity.getCnp());
        if (!studentDetails.isEmpty()) {
            throw new IllegalStateException("Student with the same CNP already exists.");
        }
        // create the new student
        StudentDetailsEntity studentDetailsEntity = new StudentDetailsEntity();
        studentDetailsEntity.setStudentJson(StudentMapper.writeValue(studentEntity));
        studentDetailsEntity.setValid(false);
        //save the student
        StudentDetailsEntity savedDetails = studentRepository.save(studentDetailsEntity);
        //add metadata obtained after save
        Student savedStudent = addMetadataToStudent(savedDetails);
        //send to validation
        validationServiceSender.validate(modelMapper.map(savedStudent, FullStudentMessage.class));
        //send to search service
        indexStudent(OperationType.CREATE, savedStudent);
        return savedStudent;
    }

    @Override
    public void update(String id, StudentEntity studentEntity) {
        Optional<StudentDetailsEntity> studentDetails = studentRepository.findById(UUID.fromString(id));
        if (studentDetails.isPresent()) {
            StudentDetailsEntity existingStudent = studentDetails.get();
            String newStudent = StudentMapper.writeValue(studentEntity);
            existingStudent.setStudentJson(newStudent);
            // update the student from the db
            StudentDetailsEntity savedEntity = studentRepository.save(existingStudent);
            // update the student from solr
            Student savedStudent = addMetadataToStudent(savedEntity);
            indexStudent(OperationType.UPDATE, savedStudent);
        } else {
            throw new NoSuchElementException("No student found to update.");
        }
    }

    @Override
    public void delete(String id) {
        Optional<StudentDetailsEntity> studentDetails = studentRepository.findById(UUID.fromString(id));
        if (studentDetails.isPresent()) {
            // delete from DB
            studentRepository.delete(studentDetails.get());
            // delete from solr
            Student studentToDelete = StudentMapper.readValue(studentDetails.get().getStudentJson(), Student.class);
            indexStudent(OperationType.DELETE, Objects.requireNonNull(studentToDelete));
        } else {
            throw new NoSuchElementException("No student found with the given id.");
        }
    }

    public void updateIsValidFlag(String id, boolean flag) {
        Optional<StudentDetailsEntity> studentDetails = studentRepository.findById(UUID.fromString(id));
        if (studentDetails.isPresent()) {
            StudentDetailsEntity studentDetailsEntity = studentDetails.get();
            studentDetailsEntity.setValid(flag);
            // update isValid from db
            StudentDetailsEntity savedEntity = studentRepository.save(studentDetailsEntity);
            // update isValid from solr
            Student savedStudent = addMetadataToStudent(savedEntity);
            indexStudent(OperationType.UPDATE, savedStudent);
        } else {
            // else simply don't update anything
            log.info("updateIsValidFlag: student not found. Update was not performed.");
        }
    }

    private Student addMetadataToStudent(StudentDetailsEntity studentDetailsEntity) {
        Student student = StudentMapper.readValue(studentDetailsEntity.getStudentJson(), Student.class);
        student.setId(studentDetailsEntity.getId().toString());
        student.setValid(studentDetailsEntity.isValid());
        return student;
    }

    private void indexStudent(OperationType operationType, Student savedStudent) {
        StudentSearch studentSearch = new StudentSearch(savedStudent.getId(),
                savedStudent.getFirstName(),
                savedStudent.getLastName(),
                savedStudent.getCnp(),
                savedStudent.isValid());
        SearchPayload searchPayload = new SearchPayload(operationType, studentSearch);
        searchServiceSender.sendPayload(searchPayload);
    }
}
