package com.rparnp.bank.model;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Account {

    @NonNull
    private UUID accountId;
    @NonNull
    private UUID customerId;
}
