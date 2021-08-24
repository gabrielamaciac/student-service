package com.learning.student.studentservice.persistance.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Grade {
    private String subject;
    private String dateReceived;
    private int mark;
}
