package com.learning.student.studentservice.integration.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.studentservice.persistance.model.Student;
import com.learning.student.studentservice.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueConsumer {

    StudentService studentService;

    public QueueConsumer(StudentService studentService) {
        this.studentService = studentService;
    }

    public void receiveMessage(String message) {
        log.info("Received (String): " + message);
        processMessage(message);
    }

    //message listener annotation
    public void receiveMessage(byte[] message) {
        String strMessage = new String(message);
        log.info("Received (No String): " + strMessage);
        processMessage(strMessage);
    }

    private void processMessage(String message) {
        try {
            Student student = new ObjectMapper().readValue(message, Student.class);
            studentService.create(student);
        } catch (JsonProcessingException e) {
            log.info("Error processing received json: " + e.getMessage());
        }

    }
}
