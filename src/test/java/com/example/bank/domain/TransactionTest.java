package com.example.bank.domain;

import com.example.bank.domain.Transaction;
import com.example.bank.port.inbound.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TransactionTest {

    private static final String REFERENCE_NUMBER = "reference-number";

    @Test
    public void shouldCreateTransactionWithCorrectData() {
        BigDecimal expectedAmount = new BigDecimal(777);
        String expectedReferenceNumber = REFERENCE_NUMBER;

        Transaction transaction = new Transaction(expectedReferenceNumber, expectedAmount);
        assertThat(transaction.getAmount()).isEqualTo(expectedAmount);
        assertThat(transaction.getReferenceNumber()).isEqualTo(expectedReferenceNumber);
    }

    @Test
    public void shouldNotCreateTransactionWithNullReferenceNumberOrAmount() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> new Transaction(null, new BigDecimal(0)));

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> new Transaction(REFERENCE_NUMBER, null));
    }

    @Test
    public void shouldNotCreateTransactionWithAmountEqualsToZero() {
        assertThatExceptionOfType(InvalidAmountException.class)
                .isThrownBy(() -> new Transaction(REFERENCE_NUMBER, new BigDecimal(0)));
    }

}
