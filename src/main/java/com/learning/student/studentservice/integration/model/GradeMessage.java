package com.learning.student.studentservice.integration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class GradeMessage {
    private String subject;
    private List<MarkMessage> marks;
}
