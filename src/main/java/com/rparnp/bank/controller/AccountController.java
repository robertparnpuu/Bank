package com.rparnp.bank.controller;

import com.rparnp.bank.exceptions.AccountNotFoundException;
import com.rparnp.bank.exceptions.InvalidCurrencyException;
import com.rparnp.bank.model.AccountRequest;
import com.rparnp.bank.model.AccountResponse;
import com.rparnp.bank.service.AccountService;
import com.rparnp.bank.util.CurrencyUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID accountId) {
        return new ResponseEntity<>(accountService.getAccount(accountId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest accountRequest) {
        if (CurrencyUtil.anyInvalid(accountRequest.getCurrencies()))
            throw new InvalidCurrencyException(InvalidCurrencyException.MESSAGE_PLURAL);

        AccountResponse response = accountService.createAccount(accountRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @ExceptionHandler({InvalidCurrencyException.class})
    public ResponseEntity<String> handleException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler({AccountNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
