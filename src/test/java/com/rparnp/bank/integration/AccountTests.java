package com.rparnp.bank.integration;

import com.rparnp.bank.controller.AccountController;
import com.rparnp.bank.entity.AccountEntity;
import com.rparnp.bank.entity.BalanceEntity;
import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.exceptions.AccountNotFoundException;
import com.rparnp.bank.exceptions.InvalidCurrencyException;
import com.rparnp.bank.mapper.AccountMapper;
import com.rparnp.bank.mapper.BalanceMapper;
import com.rparnp.bank.model.AccountRequest;
import com.rparnp.bank.model.AccountResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountTests extends BaseTest {

    private static final UUID ACCOUNT_UUID = UUID.fromString("e02a81d6-c05d-49c5-90d8-97d219cfa2b5");
    private static final UUID CUSTOMER_UUID = UUID.fromString("9de0e9fc-0bdc-4732-aa90-abd0eea05e12");
    private static final String ACCOUNT_COUNTRY = "Estonia";
    private static final List<String> CURRENCIES_EUR = List.of("EUR");
    private static final List<String> CURRENCIES_EUR_USD = List.of("EUR", "USD");
    private static final BigDecimal AMOUNT_0 = new BigDecimal("0.0");
    private static final BigDecimal AMOUNT_15 = new BigDecimal("15.0");

    @Autowired
    private AccountController accountController;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BalanceMapper balanceMapper;

    @Test
    void testCreate_Success() {
        AccountRequest request = new AccountRequest(CUSTOMER_UUID, ACCOUNT_COUNTRY, CURRENCIES_EUR);

        ResponseEntity<?> response = accountController.create(request);
        assertInstanceOf(AccountResponse.class, response.getBody());

        AccountResponse responseBody = (AccountResponse) response.getBody();
        assertNotNull(responseBody);

        UUID uuid = responseBody.getAccountId();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(CUSTOMER_UUID, responseBody.getCustomerId());
        assertEquals(1, responseBody.getBalances().size());
        assertEquals(CurrencyType.EUR, responseBody.getBalances().get(0).getCurrency());
        assertEquals(AMOUNT_0, responseBody.getBalances().get(0).getAmount());

        AccountEntity createdAccount = accountMapper.getById(uuid);
        List<BalanceEntity> createdBalances = balanceMapper.getByAccountId(uuid);

        assertNotNull(createdAccount.getAccountId());
        assertEquals(CUSTOMER_UUID, createdAccount.getCustomerId());
        assertEquals(ACCOUNT_COUNTRY, createdAccount.getCountry());
        assertEquals(1, createdBalances.size());
        assertEquals(CurrencyType.EUR, createdBalances.get(0).getCurrency());
        assertEquals(AMOUNT_0, createdBalances.get(0).getAmount());
    }

    @Test
    void testCreate_SuccessMultipleCurrencies() {
        AccountRequest request = new AccountRequest(CUSTOMER_UUID, ACCOUNT_COUNTRY, CURRENCIES_EUR_USD);

        ResponseEntity<?> response = accountController.create(request);
        AccountResponse responseBody = (AccountResponse) response.getBody();
        assertNotNull(responseBody);

        UUID uuid = responseBody.getAccountId();
        List<BalanceEntity> createdBalances = balanceMapper.getByAccountId(uuid);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2, responseBody.getBalances().size());
        assertEquals(CurrencyType.EUR, createdBalances.get(0).getCurrency());
        assertEquals(AMOUNT_0, createdBalances.get(0).getAmount());
        assertEquals(CurrencyType.USD, createdBalances.get(1).getCurrency());
        assertEquals(AMOUNT_0, createdBalances.get(1).getAmount());
    }

    @Test
    void testCreate_InvalidCurrency() {
        AccountRequest request = new AccountRequest(CUSTOMER_UUID, ACCOUNT_COUNTRY, List.of("INV"));

        assertThrows(InvalidCurrencyException.class, () -> accountController.create(request));
    }

    @Test
    void testGet_Success() {
        AccountEntity account = new AccountEntity(CUSTOMER_UUID, ACCOUNT_COUNTRY);
        accountMapper.insert(account);

        BalanceEntity balance = new BalanceEntity(account.getAccountId(), AMOUNT_15, CurrencyType.EUR);
        balanceMapper.insert(balance);
        balanceMapper.updateBalance(balance);

        ResponseEntity<?> response = accountController.getAccount(account.getAccountId());
        AccountResponse responseBody = (AccountResponse) response.getBody();
        assertNotNull(responseBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CUSTOMER_UUID, responseBody.getCustomerId());
        assertEquals(1, responseBody.getBalances().size());
        assertEquals(CurrencyType.EUR, responseBody.getBalances().get(0).getCurrency());
        assertEquals(AMOUNT_15, responseBody.getBalances().get(0).getAmount());
    }

    @Test
    void testGet_AccountNotFound() {
        assertThrows(AccountNotFoundException.class, () -> accountController.getAccount(ACCOUNT_UUID));
    }
}
