package com.learning.student.studentservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String cnp;
    private String dateOfBirth;
    private Address address;
    private List<Grade> grades;
    private boolean isValid;

    public Student(String firstName, String lastName, String cnp, String dateOfBirth, Address address, List<Grade> grades) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cnp = cnp;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.grades = grades;
    }
}

