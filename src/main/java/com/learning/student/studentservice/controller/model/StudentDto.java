package com.learning.student.studentservice.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDto {
    private String firstName;
    private String lastName;
    private String cnp;
    private String dateOfBirth;
    private AddressDto address;
    private List<GradeDto> grades;
}

