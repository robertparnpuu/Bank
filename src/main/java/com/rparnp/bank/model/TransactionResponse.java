package com.rparnp.bank.model;

import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.enums.DirectionType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    @NonNull
    protected UUID accountId;
    @NonNull
    protected UUID transactionId;
    @NonNull
    protected BigDecimal amount;
    @NonNull
    protected CurrencyType currency;
    @NonNull
    protected DirectionType direction;
    @NonNull
    protected String description;
}
