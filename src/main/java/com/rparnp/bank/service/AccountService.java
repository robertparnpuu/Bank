package com.rparnp.bank.service;

import com.rparnp.bank.entity.AccountEntity;
import com.rparnp.bank.entity.BalanceEntity;
import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.mapper.AccountMapper;
import com.rparnp.bank.mapper.BalanceMapper;
import com.rparnp.bank.model.AccountRequest;
import com.rparnp.bank.model.AccountResponse;
import com.rparnp.bank.model.BalanceResponse;
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

    public AccountResponse getAccount(UUID accountId) {
        AccountEntity accountEntity = accountMapper.getById(accountId);
        List<BalanceEntity> balanceEntities = balanceMapper.getByAccountId(accountId);

        List<BalanceResponse> balanceResponses = balanceEntities.stream()
                .map(bal -> new BalanceResponse(bal.getAmount(), bal.getCurrency())).toList();

        return new AccountResponse(accountEntity.getAccountId(), accountEntity.getCustomerId(), balanceResponses);
    }

    public AccountResponse createAccount(AccountRequest accountRequest) {
        AccountEntity accountEntity = new AccountEntity(accountRequest.getCustomerId(), accountRequest.getCountry());

        accountMapper.insert(accountEntity);
        List<BalanceResponse> balanceResponses = new ArrayList<>();

        for (String currency : accountRequest.getCurrencies()) {
            BalanceEntity balanceEntity = new BalanceEntity(accountEntity.getAccountId(),
                    new BigDecimal("0.0"), CurrencyType.valueOf(currency));

            balanceMapper.insert(balanceEntity);
            balanceResponses.add(new BalanceResponse(balanceEntity.getAmount(), balanceEntity.getCurrency()));
        }

        return new AccountResponse(accountEntity.getAccountId(), accountEntity.getCustomerId(), balanceResponses);
    }
}
