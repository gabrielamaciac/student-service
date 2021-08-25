package com.learning.student.studentservice.controller.api;

import com.learning.student.studentservice.controller.model.StudentDto;
import com.learning.student.studentservice.persistance.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface StudentApi {
    @PostMapping("")
    ResponseEntity<StudentDto> create(@RequestBody StudentDto studentDto);

    @GetMapping("")
        //pageable
    ResponseEntity<List<StudentDto>> getAll();

    @GetMapping("/{id}")
    ResponseEntity<Student> getById(@PathVariable String id);

    @PutMapping("/{id}")
    ResponseEntity<Void> updateById(@PathVariable String id, @RequestBody StudentDto studentDto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable String id);
}
