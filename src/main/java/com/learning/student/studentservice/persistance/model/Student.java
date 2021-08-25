package com.learning.student.studentservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Student implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String cnp;
    private String dateOfBirth;
    private Address address;
    private List<Grade> grades;
    private boolean isValid;
}

