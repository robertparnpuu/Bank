package com.rparnp.bank.integration;

import com.rparnp.bank.controller.TransactionController;
import com.rparnp.bank.entity.AccountEntity;
import com.rparnp.bank.entity.BalanceEntity;
import com.rparnp.bank.entity.TransactionEntity;
import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.enums.DirectionType;
import com.rparnp.bank.exceptions.*;
import com.rparnp.bank.mapper.AccountMapper;
import com.rparnp.bank.mapper.BalanceMapper;
import com.rparnp.bank.mapper.TransactionMapper;
import com.rparnp.bank.model.TransactionRequest;
import com.rparnp.bank.model.TransactionResponse;
import com.rparnp.bank.model.TransactionResponseWithRemainder;
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
public class TransactionTests extends BaseTest {

    private static final UUID CUSTOMER_UUID = UUID.fromString("9de0e9fc-0bdc-4732-aa90-abd0eea05e12");
    private static final String ACCOUNT_COUNTRY = "Estonia";
    private static final BigDecimal AMOUNT_3_5 = new BigDecimal("3.5");
    private static final BigDecimal AMOUNT_15 = new BigDecimal("15.0");
    private static final BigDecimal AMOUNT_18_5 = new BigDecimal("18.5");
    private static final String DESCRIPTION = "An average transaction for an average Joe";
    private static final String DESCRIPTION_TWO = "An excellent transaction for an excellent Moe";

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionController transactionController;

    @Test
    void testCreateIN_Success() {
        AccountEntity account = new AccountEntity(CUSTOMER_UUID, ACCOUNT_COUNTRY);
        accountMapper.insert(account);

        BalanceEntity balance = new BalanceEntity(account.getAccountId(), AMOUNT_3_5, CurrencyType.GBP);
        balanceMapper.insert(balance);
        balanceMapper.updateBalance(balance);

        TransactionRequest request = new TransactionRequest(
                account.getAccountId(), AMOUNT_15, CurrencyType.GBP.toString(),
                DirectionType.IN.toString(), DESCRIPTION);

        ResponseEntity<?> response = transactionController.create(request);
        assertInstanceOf(TransactionResponse.class, response.getBody());

        TransactionResponseWithRemainder responseBody = (TransactionResponseWithRemainder) response.getBody();
        assertNotNull(responseBody);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(responseBody.getTransactionId());
        assertEquals(AMOUNT_15, responseBody.getAmount());
        assertEquals(CurrencyType.GBP, responseBody.getCurrency());
        assertEquals(DirectionType.IN, responseBody.getDirection());
        assertEquals(AMOUNT_18_5, responseBody.getRemainingBalance());

        TransactionEntity createdTransaction = transactionMapper.getAll(account.getAccountId()).get(0);
        BalanceEntity updatedBalance = balanceMapper.getByComposite(account.getAccountId(), CurrencyType.GBP);

        assertNotNull(createdTransaction.getTransactionId());
        assertEquals(AMOUNT_15, createdTransaction.getAmount());
        assertEquals(CurrencyType.GBP, createdTransaction.getCurrency());
        assertEquals(DirectionType.IN, createdTransaction.getDirection());
        assertEquals(AMOUNT_18_5, updatedBalance.getAmount());
    }

    @Test
    void testCreateOUT_Success() {
        AccountEntity account = new AccountEntity(CUSTOMER_UUID, ACCOUNT_COUNTRY);
        accountMapper.insert(account);

        BalanceEntity balance = new BalanceEntity(account.getAccountId(), AMOUNT_18_5, CurrencyType.GBP);
        balanceMapper.insert(balance);
        balanceMapper.updateBalance(balance);

        TransactionRequest request = new TransactionRequest(
                account.getAccountId(), AMOUNT_3_5, CurrencyType.GBP.toString(),
                DirectionType.OUT.toString(), DESCRIPTION);

        ResponseEntity<?> response = transactionController.create(request);
        assertInstanceOf(TransactionResponse.class, response.getBody());

        TransactionResponseWithRemainder responseBody = (TransactionResponseWithRemainder) response.getBody();
        assertNotNull(responseBody);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(responseBody.getTransactionId());
        assertEquals(AMOUNT_3_5, responseBody.getAmount());
        assertEquals(CurrencyType.GBP, responseBody.getCurrency());
        assertEquals(DirectionType.OUT, responseBody.getDirection());
        assertEquals(AMOUNT_15, responseBody.getRemainingBalance());

        TransactionEntity createdTransaction = transactionMapper.getAll(account.getAccountId()).get(0);
        BalanceEntity updatedBalance = balanceMapper.getByComposite(account.getAccountId(), CurrencyType.GBP);

        assertNotNull(createdTransaction.getTransactionId());
        assertEquals(AMOUNT_3_5, createdTransaction.getAmount());
        assertEquals(CurrencyType.GBP, createdTransaction.getCurrency());
        assertEquals(DirectionType.OUT, createdTransaction.getDirection());
        assertEquals(AMOUNT_15, updatedBalance.getAmount());
    }

    @Test
    void testCreate_InvalidCurrency() {
        AccountEntity account = new AccountEntity(CUSTOMER_UUID, ACCOUNT_COUNTRY);
        accountMapper.insert(account);

        TransactionRequest request = new TransactionRequest(
                account.getAccountId(), AMOUNT_3_5, "INV",
                DirectionType.IN.toString(), DESCRIPTION);

        assertThrows(InvalidCurrencyException.class, () -> transactionController.create(request));
    }

    @Test
    void testCreate_InvalidDirection() {
        AccountEntity account = new AccountEntity(CUSTOMER_UUID, ACCOUNT_COUNTRY);
        accountMapper.insert(account);

        TransactionRequest request = new TransactionRequest(
                account.getAccountId(), AMOUNT_3_5, CurrencyType.GBP.toString(),
                "Sideways", DESCRIPTION);

        assertThrows(InvalidDirectionException.class, () -> transactionController.create(request));
    }

    @Test
    void testCreate_InvalidAmount() {
        AccountEntity account = new AccountEntity(CUSTOMER_UUID, ACCOUNT_COUNTRY);
        accountMapper.insert(account);

        TransactionRequest request = new TransactionRequest(
                account.getAccountId(), new BigDecimal("-12.34"), CurrencyType.GBP.toString(),
                DirectionType.IN.toString(), DESCRIPTION);

        assertThrows(InvalidAmountException.class, () -> transactionController.create(request));
    }

    @Test
    void testCreate_BalanceNotFound() {
        AccountEntity account = new AccountEntity(CUSTOMER_UUID, ACCOUNT_COUNTRY);
        accountMapper.insert(account);

        TransactionRequest request = new TransactionRequest(
                account.getAccountId(), AMOUNT_15, CurrencyType.GBP.toString(),
                DirectionType.IN.toString(), DESCRIPTION);

        assertThrows(BalanceNotFoundException.class, () -> transactionController.create(request));
    }

    @Test
    void testCreate_InsufficientFunds() {
        AccountEntity account = new AccountEntity(CUSTOMER_UUID, ACCOUNT_COUNTRY);
        accountMapper.insert(account);

        BalanceEntity balance = new BalanceEntity(account.getAccountId(), AMOUNT_3_5, CurrencyType.GBP);
        balanceMapper.insert(balance);
        balanceMapper.updateBalance(balance);

        TransactionRequest request = new TransactionRequest(
                account.getAccountId(), AMOUNT_15, CurrencyType.GBP.toString(),
                DirectionType.OUT.toString(), DESCRIPTION);

        assertThrows(InsufficientFundsException.class, () -> transactionController.create(request));
        assertEquals(AMOUNT_3_5, balanceMapper.getByComposite(account.getAccountId(), CurrencyType.GBP).getAmount());
    }

    @Test
    void testGet_Success() {
        AccountEntity account = new AccountEntity(CUSTOMER_UUID, ACCOUNT_COUNTRY);
        accountMapper.insert(account);

        BalanceEntity balanceGbp = new BalanceEntity(account.getAccountId(), AMOUNT_3_5, CurrencyType.GBP);
        balanceMapper.insert(balanceGbp);
        balanceMapper.updateBalance(balanceGbp);

        BalanceEntity balanceSek = new BalanceEntity(account.getAccountId(), AMOUNT_18_5, CurrencyType.SEK);
        balanceMapper.insert(balanceSek);
        balanceMapper.updateBalance(balanceSek);

        TransactionRequest requestGbp = new TransactionRequest(
                account.getAccountId(), AMOUNT_15, CurrencyType.GBP.toString(),
                DirectionType.IN.toString(), DESCRIPTION);

        TransactionRequest requestSek = new TransactionRequest(
                account.getAccountId(), AMOUNT_15, CurrencyType.SEK.toString(),
                DirectionType.OUT.toString(), DESCRIPTION_TWO);

        transactionController.create(requestGbp);
        transactionController.create(requestSek);

        ResponseEntity<?> response = transactionController.getAll(account.getAccountId());
        assertInstanceOf(List.class, response.getBody());

        List<TransactionResponse> responseBody = (List<TransactionResponse>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        assertEquals(requestGbp.getAccountId(), responseBody.get(0).getAccountId());
        assertEquals(requestGbp.getAmount(), responseBody.get(0).getAmount());
        assertEquals(CurrencyType.valueOf(requestGbp.getCurrency()), responseBody.get(0).getCurrency());
        assertEquals(DirectionType.valueOf(requestGbp.getDirection()), responseBody.get(0).getDirection());
        assertEquals(requestGbp.getDescription(), responseBody.get(0).getDescription());

        assertEquals(requestSek.getAccountId(), responseBody.get(1).getAccountId());
        assertEquals(requestSek.getAmount(), responseBody.get(1).getAmount());
        assertEquals(CurrencyType.valueOf(requestSek.getCurrency()), responseBody.get(1).getCurrency());
        assertEquals(DirectionType.valueOf(requestSek.getDirection()), responseBody.get(1).getDirection());
        assertEquals(requestSek.getDescription(), responseBody.get(1).getDescription());
    }

    @Test
    void testGet_AccountNotFound() {
        assertThrows(AccountNotFoundException.class, () -> transactionController.getAll(UUID.randomUUID()));
    }
}
