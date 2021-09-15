package com.learning.student.studentservice.integration.queue;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ValidationServiceSenderTest {

    AmqpTemplate jsonRabbitTemplate;
    ValidationServiceSender validationServiceSender;

    Student student = StudentTestData.getStudent();

    @BeforeEach
    void setUp() {
        jsonRabbitTemplate = mock(AmqpTemplate.class);
        validationServiceSender = new ValidationServiceSender(jsonRabbitTemplate);
    }

    @Test
    void studentIsConvertedAndSentToValidation() {
        validationServiceSender.validate(student);
        verify(jsonRabbitTemplate).convertAndSend(null, null, student);
    }
}
