package com.learning.student.studentservice.controller.api;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.facade.StudentFacade;
import com.learning.student.studentservice.util.StudentMapper;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Slf4j
public class StudentController implements StudentApi {

    private final StudentFacade studentFacade;

    public StudentController(final StudentFacade studentFacade) {
        this.studentFacade = studentFacade;
    }

    @Override
    public ResponseEntity<Student> create(@Parameter(description = "student object to be created") @RequestBody Student student) {
        Student createdStudent = studentFacade.create(StudentMapper.convertStudentToStudentEntity(student));
        log.info("Student created with id: " + createdStudent.getId());
        // Add self link
        createdStudent.add(linkTo(StudentController.class)
                .slash(createdStudent.getId())
                .withSelfRel());
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CollectionModel<Student>> getAll(@RequestParam(defaultValue = "0") int pageNo,
                                                           @RequestParam(defaultValue = "10") int pageSize) {
        List<Student> response = studentFacade.getAll(pageNo, pageSize);
        log.info(response.size() + " students found.");
        //Add self link to each student
        response.stream().forEach(s -> s.add(linkTo(StudentController.class)
                .slash(s.getId())
                .withSelfRel()));

        // Add self link to the list
        Link link = linkTo(StudentController.class).withSelfRel();
        CollectionModel<Student> result = CollectionModel.of(response, link);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Student> getById(@Parameter(description = "id of student to be searched") @PathVariable String id) {
        Student student = studentFacade.getById(id);
        log.info("Student found: " + student.getFirstName() + " " + student.getLastName());
        // Add self link
        student.add(linkTo(StudentController.class)
                .slash(student.getId())
                .withSelfRel());
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateById(@Parameter(description = "id of student to be updated") @PathVariable String id,
                                           @RequestBody Student student) {
        studentFacade.update(id, StudentMapper.convertStudentToStudentEntity(student));
        log.info("Student with id " + id + " was updated.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteById(@Parameter(description = "id of student to be deleted") @PathVariable String id) {
        studentFacade.delete(id);
        log.info("Student deleted.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
