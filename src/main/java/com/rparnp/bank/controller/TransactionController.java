package com.rparnp.bank.controller;

import com.rparnp.bank.service.TransactionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Resource
    private TransactionService transactionService;
}
