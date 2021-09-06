package com.learning.student.studentservice.integration.model.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchPayload {
    OperationType operationType;
    StudentSearch student;
}
