package com.rparnp.bank.service;

import com.rparnp.bank.entity.AccountEntity;
import com.rparnp.bank.entity.BalanceEntity;
import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.exceptions.AccountNotFoundException;
import com.rparnp.bank.mapper.AccountMapper;
import com.rparnp.bank.mapper.BalanceMapper;
import com.rparnp.bank.model.AccountRequest;
import com.rparnp.bank.model.AccountResponse;
import com.rparnp.bank.model.BalanceResponse;
import com.rparnp.bank.sender.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private RabbitMQSender rabbitMQSender;

    public AccountResponse getAccount(UUID accountId) {
        AccountEntity accountEntity = accountMapper.getById(accountId);
        if (accountEntity == null)
            throw new AccountNotFoundException();

        List<BalanceEntity> balanceEntities = balanceMapper.getByAccountId(accountId);

        List<BalanceResponse> balanceResponses = balanceEntities.stream()
                .map(bal -> new BalanceResponse(bal.getAmount(), bal.getCurrency())).toList();

        return new AccountResponse(accountEntity.getAccountId(), accountEntity.getCustomerId(), balanceResponses);
    }

    public AccountResponse createAccount(AccountRequest request) {
        rabbitMQSender.sendAccountCreation(request);
        AccountEntity accountEntity = new AccountEntity(request.getCustomerId(), request.getCountry());

        accountMapper.insert(accountEntity);
        List<BalanceResponse> balanceResponses = new ArrayList<>();

        for (String currency : request.getCurrencies()) {
            BalanceEntity balanceEntity = new BalanceEntity(accountEntity.getAccountId(),
                    new BigDecimal("0.0"), CurrencyType.valueOf(currency));

            balanceMapper.insert(balanceEntity);
            balanceResponses.add(new BalanceResponse(balanceEntity.getAmount(), balanceEntity.getCurrency()));
        }

        return new AccountResponse(accountEntity.getAccountId(), accountEntity.getCustomerId(), balanceResponses);
    }
}
