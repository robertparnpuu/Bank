package com.rparnp.bank.entity;

import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.enums.DirectionType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TransactionEntity {

    private UUID transactionId;
    @NonNull
    private UUID accountId;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private CurrencyType currency;
    @NonNull
    private DirectionType direction;
    @NonNull
    private String description;

}
