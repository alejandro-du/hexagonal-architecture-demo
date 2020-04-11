package com.example.bank.domain.util;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.port.dto.AccountDto;
import com.example.bank.port.dto.TransactionDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BankMapper {

    public Account toAccount(AccountDto dto) {
        return new Account(dto.getNumber(), dto.getPin(), dto.getTransactions().stream().map(this::toTransaction).collect(Collectors.toList()));
    }

    public Transaction toTransaction(TransactionDto dto) {
        return new Transaction(dto.getReferenceNumber(), dto.getAmount());
    }

}
