package com.rparnp.bank.exceptions;


public class InvalidCurrencyException extends RuntimeException {

    public static final String MESSAGE = "Currency is invalid.";
    public static final String MESSAGE_PLURAL = "At least one of the provided currencies is invalid.";

    public InvalidCurrencyException() {
        super(MESSAGE);
    }

    public InvalidCurrencyException(String message) {
        super(message);
    }
}
