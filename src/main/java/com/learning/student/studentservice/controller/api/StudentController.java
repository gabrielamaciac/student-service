package com.learning.student.studentservice.controller.api;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.facade.StudentFacade;
import com.learning.student.studentservice.util.StudentMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student response = studentFacade.create(StudentMapper.convertStudentToStudentEntity(student));
        log.info("Student created with id: " + response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("")
    public ResponseEntity<List<Student>> getAll(@RequestParam(defaultValue = "0") int pageNo,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        List<Student> response = studentFacade.getAll(pageNo, pageSize);
        log.info(response.size() + " students found.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable String id) {
        Student response = studentFacade.getById(id);
        log.info("Student found: " + response.getFirstName() + " " + response.getLastName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(@PathVariable String id, @RequestBody Student student) {
        studentFacade.update(id, StudentMapper.convertStudentToStudentEntity(student));
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
