package com.rparnp.bank.exceptions;

public class InvalidDirectionException extends RuntimeException {

    public static final String MESSAGE = "Direction is invalid";

    public InvalidDirectionException() {
        super(MESSAGE);
    }
}
