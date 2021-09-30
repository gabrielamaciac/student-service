package com.learning.student.studentservice.it.api;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {
    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

    @Container
    public static RabbitMQContainer rabbitMQContainer =
            new RabbitMQContainer("rabbitmq:3.8-management").withExchange("amq.direct", "direct");

}
