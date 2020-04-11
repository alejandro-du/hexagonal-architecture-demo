package com.example.bank.infrastructure.jpa;

import com.example.bank.port.dto.AccountDto;
import com.example.bank.port.dto.TransactionDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public AccountEntity toAccountEntity(AccountDto dto) {
        return new AccountEntity(null, dto.getNumber(), dto.getPin(), dto.getTransactions().stream().map(this::toTransactionEntity).collect(Collectors.toSet()));
    }

    public TransactionEntity toTransactionEntity(TransactionDto dto) {
        return new TransactionEntity(null, dto.getReferenceNumber(), dto.getAmount());
    }


}
