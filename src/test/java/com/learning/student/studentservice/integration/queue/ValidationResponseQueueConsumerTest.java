package com.learning.student.studentservice.integration.queue;

import com.learning.student.studentservice.service.StudentService;
import com.learning.student.studentservice.util.StudentTestData;
import com.learning.student.studentservice.util.ValidationTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ValidationResponseQueueConsumerTest {

    private StudentService studentService;
    private ValidationResponseQueueConsumer validationResponseQueueConsumer;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        validationResponseQueueConsumer = new ValidationResponseQueueConsumer(studentService);
    }

    @Test
    void processMessageUpdatesStudentFlag() {
        validationResponseQueueConsumer.receiveMessage(ValidationTestData.VALIDATION_RESPONSE_TRUE);

        verify(studentService).updateIsValidFlag(StudentTestData.STUDENT_ID, true);
    }

    @Test
    void processMessageSkipsUpdateStudentFlag() {
        validationResponseQueueConsumer.receiveMessage(ValidationTestData.VALIDATION_RESPONSE_FALSE);

        verify(studentService, never()).updateIsValidFlag(StudentTestData.STUDENT_ID, true);
    }
}
