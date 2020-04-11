package com.example.bank.port.inbound.exception;

public class WrongPinException extends RuntimeException {

    public WrongPinException() {
        super("Wrong PIN.");
    }

}
