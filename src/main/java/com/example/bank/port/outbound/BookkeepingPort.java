package com.example.bank.port.outbound;

import com.example.bank.port.dto.AccountDto;
import com.example.bank.port.dto.TransactionDto;

import java.util.Optional;

public interface BookkeepingPort {

    String getNewAccountNumber();

    String getNewTransactionReferenceNumber();

    void registerNewAccount(AccountDto accountDto);

    void registerNewTransaction(String accountNumber, TransactionDto transactionDto);

    Optional<AccountDto> findAccount(String number);

}
