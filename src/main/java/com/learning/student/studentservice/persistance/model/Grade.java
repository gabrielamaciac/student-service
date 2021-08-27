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
public class Grade {
    private String subject;
    private String dateReceived;
    private List<Integer> marks;
}
