package com.example.bank.port.inbound.exception;

public class InvalidAccountException extends RuntimeException {

    public InvalidAccountException(String accountNumber) {
        super(String.format("Invalid account number %s.", accountNumber));
    }

}
