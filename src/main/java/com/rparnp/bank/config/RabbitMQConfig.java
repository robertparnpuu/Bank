package com.rparnp.bank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ACCOUNT_QUEUE = "q.account-creation";
    public static final String TRANSACTION_QUEUE = "q.transaction-creation";

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean
    public Queue createAccountQueue() {
        return new Queue(ACCOUNT_QUEUE, false); // Non-durable Queue because of application's temporary state
    }

    @Bean
    public Queue createTransactionQueue() {
        return new Queue(TRANSACTION_QUEUE, false); // Non-durable Queue because of application's temporary state
    }
}
