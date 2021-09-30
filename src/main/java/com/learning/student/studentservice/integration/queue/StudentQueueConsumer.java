package com.learning.student.studentservice.integration.queue;

import com.learning.student.studentservice.integration.model.StudentMessage;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.service.StudentService;
import com.learning.student.studentservice.util.GenericMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Consumes the Student from student-importer-service and creates it in the DB.
 */
@Component
@Slf4j
public class StudentQueueConsumer {

    private final StudentService studentService;

    private final ModelMapper modelMapper;

    public StudentQueueConsumer(StudentService studentService, ModelMapper modelMapper) {
        this.studentService = studentService;
        this.modelMapper = modelMapper;
    }

    @RabbitListener(queues = "student-queue")
    public void receiveMessage(String message) {
        processMessage(message);
    }

    private void processMessage(String message) {
        log.info("Processing message: " + message);
        StudentMessage studentMessage = GenericMapper.readValue(message, StudentMessage.class);
        StudentEntity studentEntity = modelMapper.map(studentMessage, StudentEntity.class);
        if (!Objects.isNull(studentEntity)) {
            studentService.create(studentEntity);
            log.info("Student created from queue.");
        } else {
            log.error("Student could not be created from queue.");
        }
    }
}
