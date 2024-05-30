package com.rparnp.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AccountMessageEntity {

    private UUID accountId;
    private UUID customerId;
    private String country;
    private List<String> currencies;
}
