package com.rparnp.bank.controller;

import com.rparnp.bank.enums.DirectionType;
import com.rparnp.bank.exceptions.*;
import com.rparnp.bank.model.TransactionRequest;
import com.rparnp.bank.model.TransactionResponse;
import com.rparnp.bank.model.TransactionResponseWithRemainder;
import com.rparnp.bank.service.TransactionService;
import com.rparnp.bank.util.CurrencyUtil;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Resource
    private TransactionService transactionService;

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getAll(@PathVariable UUID accountId) {
        List<TransactionResponse> response = transactionService.getAll(accountId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseWithRemainder> create(@RequestBody TransactionRequest transactionRequest) {
        if (CurrencyUtil.isInvalid(transactionRequest.getCurrency()))
            throw new InvalidCurrencyException();
        if (!ObjectUtils.containsConstant(DirectionType.values(), transactionRequest.getDirection()))
            throw new InvalidDirectionException();
        if (transactionRequest.getAmount().compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidAmountException();

        TransactionResponseWithRemainder response = transactionService.create(transactionRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @ExceptionHandler({
            InvalidCurrencyException.class,
            InvalidDirectionException.class,
            InsufficientFundsException.class
    })
    public ResponseEntity<String> handleException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler({
            AccountNotFoundException.class,
            BalanceNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
