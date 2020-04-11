package com.example.bank.domain;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.port.inbound.exception.InsufficientFundsException;
import com.example.bank.port.inbound.exception.WrongPinException;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class AccountTest {

    private static final String SOME_ACCOUNT_NUMBER = "account-number";
    private static final String SOME_PIN = "0000";
    private static final String SOME_REFERENCE_NUMBER = "reference-number";

    @Test
    public void shouldCreateAccountWithCorrectData() {
        String expectedNumber = SOME_ACCOUNT_NUMBER;
        String expectedPin = "7777";

        Account account = new Account(expectedNumber, expectedPin);
        assertThat(account.getNumber()).isEqualTo(expectedNumber);
        assertThat(account.getPin()).isEqualTo(expectedPin);
    }

    @Test
    public void shouldNotCreateAccountWithNullNumberOrNullPin() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> new Account(null, SOME_PIN));

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> new Account(SOME_ACCOUNT_NUMBER, null));
    }

    @Test
    public void shouldHaveCorrectBalanceAfterPerformingTransactions() {
        Account account = new Account(SOME_ACCOUNT_NUMBER, SOME_PIN);

        account.performTransaction(new Transaction(SOME_REFERENCE_NUMBER, new BigDecimal(2)), SOME_PIN);
        account.performTransaction(new Transaction(SOME_REFERENCE_NUMBER, new BigDecimal(3)), SOME_PIN);
        assertThat(account.getBalance()).isEqualTo(new BigDecimal(5));
    }

    @Test
    public void shouldThrowExceptionWhenInsufficientFunds() {
        Account account = new Account(SOME_ACCOUNT_NUMBER, SOME_PIN);
        BigDecimal funds = new BigDecimal(5);
        BigDecimal transactionAmount = new BigDecimal(-10);
        account.performTransaction(new Transaction(SOME_REFERENCE_NUMBER, funds), SOME_PIN);

        assertThatExceptionOfType(InsufficientFundsException.class)
                .isThrownBy(() -> {
                    Transaction transaction = new Transaction(SOME_REFERENCE_NUMBER, transactionAmount);
                    account.performTransaction(transaction, SOME_PIN);
                });
    }

    @Test
    public void shouldThrowExceptionWhenPerformingTransactionWithWrongPin() {
        String correctPin = "7777";
        String wrongPin = SOME_PIN;
        assert correctPin != wrongPin;
        Account account = new Account(SOME_ACCOUNT_NUMBER, correctPin);

        assertThatExceptionOfType(WrongPinException.class)
                .isThrownBy(() -> {
                    Transaction transaction = new Transaction(SOME_REFERENCE_NUMBER, new BigDecimal(777));
                    account.performTransaction(transaction, wrongPin);
                });
    }

}
