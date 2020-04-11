package com.example.bank.port.inbound.exception;

public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException() {
        super("Invalid amount.");
    }

}
