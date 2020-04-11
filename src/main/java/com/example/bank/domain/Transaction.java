package com.example.bank.domain;

import com.example.bank.domain.util.Validator;
import com.example.bank.port.dto.TransactionDto;
import com.example.bank.port.inbound.exception.InvalidAmountException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {

    @Getter
    @EqualsAndHashCode.Include
    @NotNull
    private String referenceNumber;

    @Getter
    @EqualsAndHashCode.Include
    @NotNull
    private final BigDecimal amount;

    public Transaction(String referenceNumber, BigDecimal amount) {
        this.referenceNumber = referenceNumber;
        this.amount = amount;

        Validator.validate(this);
        if (amount.equals(BigDecimal.ZERO)) {
            throw new InvalidAmountException();
        }
    }

    public TransactionDto toDto() {
        return new TransactionDto(referenceNumber, amount);
    }

}
