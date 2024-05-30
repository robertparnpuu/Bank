package com.rparnp.bank.service;

import com.rparnp.bank.entity.AccountEntity;
import com.rparnp.bank.entity.BalanceEntity;
import com.rparnp.bank.entity.TransactionEntity;
import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.enums.DirectionType;
import com.rparnp.bank.exceptions.AccountNotFoundException;
import com.rparnp.bank.exceptions.BalanceNotFoundException;
import com.rparnp.bank.exceptions.InsufficientFundsException;
import com.rparnp.bank.mapper.AccountMapper;
import com.rparnp.bank.mapper.BalanceMapper;
import com.rparnp.bank.mapper.TransactionMapper;
import com.rparnp.bank.model.TransactionMessageEntity;
import com.rparnp.bank.model.TransactionRequest;
import com.rparnp.bank.model.TransactionResponse;
import com.rparnp.bank.model.TransactionResponseWithRemainder;
import com.rparnp.bank.sender.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private RabbitMQSender rabbitMQSender;

    public List<TransactionResponse> getAll(UUID accountId) {
        AccountEntity account = accountMapper.getById(accountId);
        if (account == null)
            throw new AccountNotFoundException();

        List<TransactionEntity> transactions = transactionMapper.getAll(accountId);

        return transactions.stream().map(t ->
                new TransactionResponse(t.getAccountId(), t.getTransactionId(), t.getAmount(),
                        t.getCurrency(), t.getDirection(), t.getDescription())).toList();
    }

    public TransactionResponseWithRemainder create(TransactionRequest request) {
        BalanceEntity balance = balanceMapper.getByComposite(request.getAccountId(),
                CurrencyType.valueOf(request.getCurrency()));
        if (balance == null)
            throw new BalanceNotFoundException();

        TransactionEntity transaction;
        BigDecimal newBalance = BigDecimal.ZERO;

        if (DirectionType.valueOf(request.getDirection()).equals(DirectionType.IN)) {
            newBalance = balance.getAmount().add(request.getAmount());

        } else if (DirectionType.valueOf(request.getDirection()).equals(DirectionType.OUT)) {
            newBalance = balance.getAmount().subtract(request.getAmount());
            if (newBalance.compareTo(BigDecimal.ZERO) < 0)
                throw new InsufficientFundsException();
        }

        balance.setAmount(newBalance);
        balanceMapper.updateBalance(balance);

        transaction = new TransactionEntity(request.getAccountId(),
                request.getAmount(),
                CurrencyType.valueOf(request.getCurrency()),
                DirectionType.valueOf(request.getDirection()),
                request.getDescription());

        transactionMapper.create(transaction);
        rabbitMQSender.sendTransactionCreation(new TransactionMessageEntity(
                transaction.getTransactionId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDirection(),
                transaction.getDescription()
        ));

        return new TransactionResponseWithRemainder(transaction.getAccountId(),
                transaction.getTransactionId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDirection(),
                transaction.getDescription(),
                newBalance);
    }
}
