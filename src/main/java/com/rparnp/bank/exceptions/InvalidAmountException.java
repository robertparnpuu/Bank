package com.rparnp.bank.exceptions;

public class InvalidAmountException extends RuntimeException {

    public static final String MESSAGE = "Amount is invalid.";

    public InvalidAmountException() {
        super(MESSAGE);
    }
}
