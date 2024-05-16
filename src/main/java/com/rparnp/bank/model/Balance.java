package com.rparnp.bank.model;

import com.rparnp.bank.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Balance {

    @NonNull
    private UUID accountId;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private CurrencyType currency;
}
