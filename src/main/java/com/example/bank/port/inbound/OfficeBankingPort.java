package com.example.bank.port.inbound;

import java.math.BigDecimal;

public interface OfficeBankingPort {

    String openNewAccount(String pin);

    String withdraw(String accountNumber, BigDecimal amount, String pin);

    String deposit(String accountNumber, BigDecimal amount);

    String transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String pin);

}
