package com.learning.student.studentservice.controller.api;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.facade.StudentFacade;
import com.learning.student.studentservice.util.StudentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Student Service", description = "The student service API with CRUD operations.")
@RequestMapping("/student")
public class StudentController implements StudentApi {

    private final StudentFacade studentFacade;

    public StudentController(final StudentFacade studentFacade) {
        this.studentFacade = studentFacade;
    }

    @Override
    @Operation(summary = "Create a student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))}),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Student already exists.", content = @Content)})
    @PostMapping("")
    public ResponseEntity<Student> create(@Parameter(description = "student object to be created") @RequestBody Student student) {
        Student response = studentFacade.create(StudentMapper.convertStudentToStudentEntity(student));
        log.info("Student created with id: " + response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @Operation(summary = "Get all students.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students found.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Student.class)))})})
    @GetMapping("")
    public ResponseEntity<List<Student>> getAll(@RequestParam(defaultValue = "0") int pageNo,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        List<Student> response = studentFacade.getAll(pageNo, pageSize);
        log.info(response.size() + " students found.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Get a student by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the student.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Student.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@Parameter(description = "id of student to be searched") @PathVariable String id) {
        Student response = studentFacade.getById(id);
        log.info("Student found: " + response.getFirstName() + " " + response.getLastName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Update a student by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student updated successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))}),
            @ApiResponse(responseCode = "404", description = "No student exists with given id.", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(@Parameter(description = "id of student to be updated") @PathVariable String id,
                                           @RequestBody Student student) {
        studentFacade.update(id, StudentMapper.convertStudentToStudentEntity(student));
        log.info("Student with id " + id + " was updated.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @Operation(summary = "Delete a student by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deleted."),
            @ApiResponse(responseCode = "404", description = "No student exists with the given id.", content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "id of student to be deleted") @PathVariable String id) {
        studentFacade.delete(id);
        log.info("Student deleted.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
