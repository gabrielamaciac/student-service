package com.learning.student.studentservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class GradeEntity {
    private String subject;
    private String dateReceived;
    private Double mark;
}
