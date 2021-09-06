package com.learning.student.studentservice.configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.studentqueue}")
    private String studentQueue;
    @Value("${spring.rabbitmq.validationqueue}")
    private String validationQueue;
    @Value("${spring.rabbitmq.responsequeue}")
    private String responseQueue;
    @Value("${spring.rabbitmq.studentrouting}")
    private String studentRoutingKey;
    @Value("${spring.rabbitmq.validationrouting}")
    private String validationRoutingKey;
    @Value("${spring.rabbitmq.responserouting}")
    private String responseRouting;

    @Value("${spring.rabbitmq.searchqueue}")
    private String searchQueue;
    @Value("${spring.rabbitmq.searchrouting}")
    private String searchRoutingKey;

    @Bean
    Queue studentQueue() {
        return new Queue(studentQueue, false);
    }

    @Bean
    Queue validationQueue() {
        return new Queue(validationQueue, false);
    }

    @Bean
    Queue responseQueue() {
        return new Queue(responseQueue, false);
    }

    @Bean
    Queue searchQueue() {
        return new Queue(searchQueue, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding(Queue studentQueue, DirectExchange exchange) {
        return BindingBuilder.bind(studentQueue).to(exchange).with(studentRoutingKey);
    }

    @Bean
    public Binding validationBinding(Queue validationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(validationQueue).to(exchange).with(validationRoutingKey);
    }

    @Bean
    public Binding responseBinding(Queue responseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(responseQueue).to(exchange).with(responseRouting);
    }

    @Bean
    public Binding searchBinding(Queue searchQueue, DirectExchange exchange) {
        return BindingBuilder.bind(searchQueue).to(exchange).with(searchRoutingKey);
    }

    @Bean
    public AmqpTemplate jsonRabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
