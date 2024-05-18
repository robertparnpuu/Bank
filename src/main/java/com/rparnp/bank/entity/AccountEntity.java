package com.rparnp.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountEntity {

    private UUID accountId;
    @NonNull
    private UUID customerId;
    @NonNull
    private String country;
}
