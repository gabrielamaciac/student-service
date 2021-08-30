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
public class StudentEntity {
    private String firstName;
    private String lastName;
    private String cnp;
    private String dateOfBirth;
    private AddressEntity address;
    private List<GradeEntity> grades;
}

