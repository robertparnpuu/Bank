package com.rparnp.bank.model;

import com.rparnp.bank.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceResponse {

    private BigDecimal amount;
    private CurrencyType currency;
}
