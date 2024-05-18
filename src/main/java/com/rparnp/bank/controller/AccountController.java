package com.rparnp.bank.controller;

import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.exceptions.AccountNotFoundException;
import com.rparnp.bank.exceptions.InvalidCurrencyException;
import com.rparnp.bank.model.AccountRequest;
import com.rparnp.bank.model.AccountResponse;
import com.rparnp.bank.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
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
    public ResponseEntity<?> create(@Valid @RequestBody AccountRequest accountRequest) {
        if (accountRequest.getCurrencies().stream().anyMatch(c ->
                !ObjectUtils.containsConstant(CurrencyType.values(), c))) {
            throw new InvalidCurrencyException("At least one of the provided currencies is invalid.");
        }

        AccountResponse response = accountService.createAccount(accountRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @ExceptionHandler({InvalidCurrencyException.class, AccountNotFoundException.class})
    public ResponseEntity<String> handleException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
