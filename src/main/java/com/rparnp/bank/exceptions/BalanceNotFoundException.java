package com.rparnp.bank.exceptions;

public class BalanceNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Balance not found.";

    public BalanceNotFoundException() {
        super(MESSAGE);
    }
}
