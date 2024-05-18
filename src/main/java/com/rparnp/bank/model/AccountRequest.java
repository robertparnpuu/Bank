package com.rparnp.bank.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AccountRequest {

    private UUID customerId;
    private String country;
    private List<String> currencies;
}
