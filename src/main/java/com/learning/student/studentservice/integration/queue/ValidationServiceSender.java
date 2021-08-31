package com.learning.student.studentservice.integration.queue;

import com.learning.student.studentservice.controller.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Sends the Student object to validation-service via validation-queue.
 */
@Component
@Slf4j
public class ValidationServiceSender {

    @Value("${spring.rabbitmq.validationrouting}")
    private String routingKey;

    @Autowired
    private AmqpTemplate jsonRabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    public void validate(Student student) {
        jsonRabbitTemplate.convertAndSend(exchange, routingKey, student);
        log.info("Student sent to validation: " + student.getFirstName());
    }
}