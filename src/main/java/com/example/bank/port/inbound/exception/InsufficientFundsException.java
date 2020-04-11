package com.example.bank.port.inbound.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
        super("Insufficient funds.");
    }

}
