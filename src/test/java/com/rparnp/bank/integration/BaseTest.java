package com.rparnp.bank.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@EnableConfigurationProperties
@ActiveProfiles("test")
public class BaseTest {

    @TestConfiguration
    static class TestRabbitMQConfig {

        @Bean
        @Primary
        public RabbitTemplate rabbitTemplate() {
            CachingConnectionFactory connectionFactory =
                    new CachingConnectionFactory(rabbitMQContainer.getHost(), rabbitMQContainer.getAmqpPort());
            return new RabbitTemplate(connectionFactory);
        }
    }

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("schema.sql");

    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management-alpine");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
        rabbitMQContainer.start();

        System.setProperty("DB_URL", postgreSQLContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgreSQLContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgreSQLContainer.getPassword());
        System.setProperty("RMQ_URL", rabbitMQContainer.getHost());
        System.setProperty("RMQ_PORT", rabbitMQContainer.getAmqpPort().toString());
    }
}
