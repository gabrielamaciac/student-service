package com.learning.student.studentservice.it.api.testcontainers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {
    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();
}
