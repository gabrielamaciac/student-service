package com.learning.student.studentservice.integration.queue;

import com.learning.student.studentservice.integration.model.StudentMessage;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.service.StudentService;
import com.learning.student.studentservice.util.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes the Student from student-importer-service and creates it in the DB.
 */
@Component
@Slf4j
public class StudentQueueConsumer {

    private final StudentService studentService;

    public StudentQueueConsumer(StudentService studentService) {
        this.studentService = studentService;
    }

    @RabbitListener(queues = "student-queue")
    public void receiveMessage(String message) {
        processMessage(message);
    }

    private void processMessage(String message) {
        log.info("Processing message: " + message);
        StudentMessage studentMessage = StudentMapper.readValue(message, StudentMessage.class);
        // TODO optional?
        studentService.create(StudentMapper.map(studentMessage, StudentEntity.class));
        log.info("Student created from queue.");
    }
}
