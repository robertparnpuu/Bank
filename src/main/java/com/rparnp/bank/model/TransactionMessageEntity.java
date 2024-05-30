package com.rparnp.bank.model;

import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.enums.DirectionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TransactionMessageEntity {

    private UUID transactionId;
    private UUID accountId;
    private BigDecimal amount;
    private CurrencyType currency;
    private DirectionType direction;
    private String description;
}
