package com.rparnp.bank.model;

import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.enums.DirectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Transaction {

    @NonNull
    private UUID transactionId;
    @NonNull
    private UUID accountId;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private CurrencyType currency;
    @NonNull
    private DirectionType direction;
    private String description;

}
