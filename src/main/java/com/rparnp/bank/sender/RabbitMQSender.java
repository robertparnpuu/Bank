package com.rparnp.bank.sender;

import com.rparnp.bank.config.RabbitMQConfig;
import com.rparnp.bank.model.AccountRequest;
import com.rparnp.bank.model.TransactionRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendAccountCreation(AccountRequest accountRequest) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ACCOUNT_QUEUE, accountRequest);
    }

    public void sendTransactionCreation(TransactionRequest transactionRequest) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.TRANSACTION_QUEUE, transactionRequest);
    }
}
