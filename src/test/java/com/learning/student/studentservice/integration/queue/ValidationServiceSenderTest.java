package com.learning.student.studentservice.integration.queue;

import com.learning.student.studentservice.integration.model.FullStudentMessage;
import com.learning.student.studentservice.util.StudentMapper;
import com.learning.student.studentservice.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ValidationServiceSenderTest {

    private AmqpTemplate jsonRabbitTemplate;
    private ValidationServiceSender validationServiceSender;

    @BeforeEach
    void setUp() {
        jsonRabbitTemplate = mock(AmqpTemplate.class);
        validationServiceSender = new ValidationServiceSender(jsonRabbitTemplate);
    }

    @Test
    void studentIsConvertedAndSentToValidation() {
        FullStudentMessage fullStudentMessage = StudentMapper.map(StudentTestData.getStudent(), FullStudentMessage.class);
        validationServiceSender.validate(fullStudentMessage);
        verify(jsonRabbitTemplate).convertAndSend(null, null, fullStudentMessage);
    }
}
