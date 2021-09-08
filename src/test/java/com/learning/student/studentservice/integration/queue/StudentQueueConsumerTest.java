package com.learning.student.studentservice.integration.queue;

import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.service.StudentService;
import com.learning.student.studentservice.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StudentQueueConsumerTest {
    private StudentService studentService;
    private StudentQueueConsumer studentQueueConsumer;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        studentQueueConsumer = new StudentQueueConsumer(studentService);
    }

    @Test
    void processMessageIsSuccessful() {
        studentQueueConsumer.receiveMessage(StudentTestData.STUDENT_JSON);

        verify(studentService).create(any(StudentEntity.class));
    }
}
