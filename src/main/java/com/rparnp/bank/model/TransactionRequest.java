package com.rparnp.bank.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequest {

    private UUID accountId;
    private BigDecimal amount;
    private String currency;
    private String direction;
    private String description;
}
