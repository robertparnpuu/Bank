package com.rparnp.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AccountResponse {

    private UUID accountId;
    private UUID customerId;
    private List<BalanceResponse> balances;
}
