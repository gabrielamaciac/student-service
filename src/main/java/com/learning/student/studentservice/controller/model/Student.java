package com.learning.student.studentservice.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student extends RepresentationModel<Student> {
    private String id;
    private String firstName;
    private String lastName;
    private String cnp;
    private String dateOfBirth;
    private Address address;
    private List<Grade> grades;
    private boolean isValid;
}

