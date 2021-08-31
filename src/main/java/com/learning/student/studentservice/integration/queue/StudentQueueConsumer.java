package com.learning.student.studentservice.integration.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Consumes the Student from student-importer-service and creates it in the DB.
 */
@Component
@Slf4j
public class StudentQueueConsumer {

    @Autowired
    StudentService studentService;

    ObjectMapper objectMapper = new ObjectMapper();

    public StudentQueueConsumer(StudentService studentService) {
        this.studentService = studentService;
    }

    @RabbitListener(queues = "student-queue")
    public void receiveMessage(String message) {
        processMessage(message);
    }

    private void processMessage(String message) {
        try {
            log.info("Processing message: " + message);
            StudentEntity studentMessage = objectMapper.readValue(message, StudentEntity.class);
//            studentService.create(StudentMapper.convertStudentMessageToStudentEntity(studentMessage));
            studentService.create(studentMessage);
            log.info("Student created from queue.");
        } catch (JsonProcessingException e) {
            log.error("Error processing received json: " + e);
        }
    }
}
