package com.learning.student.studentservice.integration.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.studentservice.integration.model.ValidationResponse;
import com.learning.student.studentservice.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Consumes the ValidationResponse from validation-service and updates the isValid flag.
 */
@Component
@Slf4j
public class ValidationResponseConsumer {
    @Autowired
    StudentService studentService;

    ObjectMapper objectMapper = new ObjectMapper();

    public ValidationResponseConsumer(StudentService studentService) {
        this.studentService = studentService;
    }

    @RabbitListener(queues = "validation-response-queue")
    public void receiveMessage(String message) {
        processMessage(message);
    }

    private void processMessage(String message) {
        try {
            log.info("Processing message: " + message);
            ValidationResponse validationResponse = objectMapper.readValue(message, ValidationResponse.class);
            log.info("ValidationResponse received: isValid " + validationResponse.isValid());
            if (validationResponse.isValid()) {
                studentService.updateIsValidFlag(validationResponse.getStudentId(), validationResponse.isValid());
                log.info("Updated flag for student with id: " + validationResponse.getStudentId());
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing received json: " + e);
        }
    }
}
