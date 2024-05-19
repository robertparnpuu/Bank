package com.rparnp.bank.exceptions;

public class InsufficientFundsException extends RuntimeException {

    public static final String MESSAGE = "Insufficient funds on balance.";

    public InsufficientFundsException() {
        super(MESSAGE);
    }
}
