package com.example.bank.domain;

import com.example.bank.domain.util.Validator;
import com.example.bank.port.dto.AccountDto;
import com.example.bank.port.inbound.exception.InsufficientFundsException;
import com.example.bank.port.inbound.exception.WrongPinException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Getter
    @EqualsAndHashCode.Include
    @NotNull
    private final String number;

    @Getter
    @NotNull
    private String pin;

    @NotNull
    private final List<Transaction> transactions;

    public Account(String number, String pin, List<Transaction> transactions) {
        this.number = number;
        this.pin = pin;
        this.transactions = transactions;
        Validator.validate(this);
    }

    public Account(String number, String pin) {
        this(number, pin, new ArrayList<>());
    }

    public AccountDto toDto() {
        return new AccountDto(number, pin, transactions.stream().map(Transaction::toDto).collect(Collectors.toList()));
    }

    public synchronized void performTransaction(Transaction transaction, String pin) {
        checkPin(pin);
        checkEnoughFunds(transaction.getAmount());
        transactions.add(transaction);
    }

    public BigDecimal getBalance() {
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void checkPin(String pin) {
        if (!getPin().equals(pin)) {
            throw new WrongPinException();
        }
    }

    private void checkEnoughFunds(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            if (amount.negate().compareTo(getBalance()) > 0) {
                throw new InsufficientFundsException();
            }
        }
    }

}
