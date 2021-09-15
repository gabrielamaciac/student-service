package com.learning.student.studentservice.controller.api;

import com.learning.student.studentservice.controller.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface StudentApi {
    @PostMapping("")
    ResponseEntity<Student> create(@RequestBody Student student);

    @GetMapping("")
    ResponseEntity<List<Student>> getAll(@RequestParam int pageNo, @RequestParam int pageSize);

    @GetMapping("/{id}")
    ResponseEntity<Student> getById(@PathVariable String id);

    @PutMapping("/{id}")
    ResponseEntity<Void> updateById(@PathVariable String id, @RequestBody Student student);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable String id);
}
