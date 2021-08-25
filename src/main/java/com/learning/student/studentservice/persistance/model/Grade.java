package com.learning.student.studentservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Grade implements Serializable {
    private String subject;
    private String dateReceived;
    private int mark;
}
