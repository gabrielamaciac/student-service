package com.learning.student.studentservice.controller.api;

import com.learning.student.studentservice.controller.model.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Tag(name = "Student Service", description = "The student service API with CRUD operations.")
@RequestMapping("/student")
public interface StudentApi {
    @Operation(summary = "Create a student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))}),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Student already exists.", content = @Content)})
    @PostMapping("")
    ResponseEntity<Student> create(@Parameter(description = "student object to be created") @RequestBody @Valid Student student);

    @Operation(summary = "Get all students.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students found.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Student.class)))})})
    @GetMapping("")
    ResponseEntity<CollectionModel<Student>> getAll(@RequestParam(defaultValue = "0") int pageNo,
                                                    @RequestParam(defaultValue = "10") int pageSize);

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
    ResponseEntity<Student> getById(@Parameter(description = "id of student to be searched") @PathVariable @NotBlank String id);

    @Operation(summary = "Update a student by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student updated successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))}),
            @ApiResponse(responseCode = "404", description = "No student exists with given id.", content = @Content)})
    @PutMapping("/{id}")
    ResponseEntity<Void> updateById(@Parameter(description = "id of student to be updated") @PathVariable @NotBlank String id,
                                    @RequestBody @Valid Student student);

    @Operation(summary = "Delete a student by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deleted."),
            @ApiResponse(responseCode = "404", description = "No student exists with the given id.", content = @Content)})
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@Parameter(description = "id of student to be deleted") @PathVariable @NotBlank String id);
}
