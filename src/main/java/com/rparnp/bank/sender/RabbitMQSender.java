package com.rparnp.bank.sender;

import com.rparnp.bank.config.RabbitMQConfig;
import com.rparnp.bank.model.AccountMessageEntity;
import com.rparnp.bank.model.TransactionMessageEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendAccountCreation(AccountMessageEntity accountEntity) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ACCOUNT_QUEUE, accountEntity);
    }

    public void sendTransactionCreation(TransactionMessageEntity transactionEntity) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.TRANSACTION_QUEUE, transactionEntity);
    }
}
