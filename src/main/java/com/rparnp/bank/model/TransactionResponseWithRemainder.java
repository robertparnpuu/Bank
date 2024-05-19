package com.rparnp.bank.model;

import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.enums.DirectionType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionResponseWithRemainder extends TransactionResponse {

    @NonNull
    private BigDecimal remainingBalance;

    public TransactionResponseWithRemainder(UUID accountId, UUID transactionId, BigDecimal amount, CurrencyType currency,
                                            DirectionType direction, String description, @NonNull BigDecimal newBalance) {
        super(accountId, transactionId, amount, currency, direction, description);
        this.remainingBalance = newBalance;
    }
}
