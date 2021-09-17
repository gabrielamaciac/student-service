package com.learning.student.studentservice.it.api;

import com.github.fridujo.rabbitmq.mock.compatibility.MockConnectionFactoryFactory;
import com.learning.student.studentservice.configuration.RabbitConfiguration;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RabbitConfiguration.class)
class AmpqTestConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(MockConnectionFactoryFactory.build());
    }
}

