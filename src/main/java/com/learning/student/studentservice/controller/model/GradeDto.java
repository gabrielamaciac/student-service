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
public class GradeDto {
    //(discipline, date of grade, grade)
    private String subject;
    private String dateReceived;
    private List<Integer> marks;
}
