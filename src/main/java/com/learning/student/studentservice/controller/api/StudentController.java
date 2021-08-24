package com.learning.student.studentservice.controller.api;

import com.learning.student.studentservice.controller.model.StudentDto;
import com.learning.student.studentservice.facade.StudentService;
import com.learning.student.studentservice.service.util.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(final StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody StudentDto studentDto) {
        String response = studentService.create(StudentMapper.convertStudentDtoToStudent(studentDto));
        log.info("Json of the created student: " + response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable String id) {
        String response = studentService.getById(id);
        log.info("Student found: " + response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
