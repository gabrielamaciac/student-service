package com.learning.student.studentservice.integration.queue;

import com.learning.student.studentservice.integration.model.search.SearchPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SearchServiceSender {
    @Value("${spring.rabbitmq.searchrouting}")
    private String routingKey;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    private AmqpTemplate jsonRabbitTemplate;

    public SearchServiceSender(AmqpTemplate jsonRabbitTemplate) {
        this.jsonRabbitTemplate = jsonRabbitTemplate;
    }

    public void sendPayload(SearchPayload payload) {
        jsonRabbitTemplate.convertAndSend(exchange, routingKey, payload);
        log.info("Student payload sent to search service: " + payload.getOperationType() + " on " + payload.getStudent().getFirstName());
    }
}
