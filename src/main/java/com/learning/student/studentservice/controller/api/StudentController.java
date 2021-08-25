package com.learning.student.studentservice.controller.api;

import com.learning.student.studentservice.controller.model.StudentDto;
import com.learning.student.studentservice.facade.StudentFacade;
import com.learning.student.studentservice.persistance.model.Student;
import com.learning.student.studentservice.service.util.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/student")
public class StudentController implements StudentApi {

    private final StudentFacade studentFacade;

    public StudentController(final StudentFacade studentFacade) {
        this.studentFacade = studentFacade;
    }

    @Override
    @PostMapping("")
    public ResponseEntity<StudentDto> create(@RequestBody StudentDto studentDto) {
        Student response = studentFacade.create(StudentMapper.convertStudentDtoToStudent(studentDto));
        log.info("Json of the created student: " + response);
        return new ResponseEntity<>(StudentMapper.convertStudentToStudentDto(response), HttpStatus.CREATED);
    }

    @Override
    @GetMapping("")
    //pageable
    public ResponseEntity<List<StudentDto>> getAll() {
        List<Student> response = studentFacade.getAll();
        log.info(response.size() + " students found.");
        List<StudentDto> students = response.stream().map(StudentMapper::convertStudentToStudentDto).collect(Collectors.toList());
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable String id) {
        Student response = studentFacade.getById(id);
        log.info("Student found: " + response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(@PathVariable String id, @RequestBody StudentDto studentDto) {
        studentFacade.update(id, StudentMapper.convertStudentDtoToStudent(studentDto));
        log.info("Student with id " + id + " was updated.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        studentFacade.delete(id);
        log.info("Student deleted.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
