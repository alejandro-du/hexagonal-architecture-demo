package com.example.bank.domain;

import com.example.bank.domain.util.BankMapper;
import com.example.bank.port.dto.AccountDto;
import com.example.bank.port.inbound.OfficeBankingPort;
import com.example.bank.port.inbound.OnlineBankingPort;
import com.example.bank.port.inbound.exception.InvalidAccountException;
import com.example.bank.port.inbound.exception.InvalidAmountException;
import com.example.bank.port.outbound.BookkeepingPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class Bank implements OfficeBankingPort, OnlineBankingPort {

    private final BookkeepingPort bookkeeping;
    private final BankMapper bankMapper;

    public Bank(BookkeepingPort bookkeeping, BankMapper bankMapper) {
        this.bookkeeping = bookkeeping;
        this.bankMapper = bankMapper;
    }

    @Override
    public String openNewAccount(String pin) {
        Account account = new Account(bookkeeping.getNewAccountNumber(), pin);
        bookkeeping.registerNewAccount(account.toDto());
        return account.getNumber();
    }

    @Override
    public String deposit(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        String referenceNumber = bookkeeping.getNewTransactionReferenceNumber();
        doTransaction(accountNumber, amount, true, null, referenceNumber);
        return referenceNumber;
    }

    @Override
    public String withdraw(String accountNumber, BigDecimal amount, String pin) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        String referenceNumber = bookkeeping.getNewTransactionReferenceNumber();
        doTransaction(accountNumber, amount.negate(), false, pin, referenceNumber);
        return referenceNumber;
    }

    @Override
    @Transactional
    public String transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String pin) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new InvalidAccountException(toAccountNumber);
        } else if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        String referenceNumber = bookkeeping.getNewTransactionReferenceNumber();
        doTransaction(fromAccountNumber, amount.negate(), false, pin, referenceNumber);
        doTransaction(toAccountNumber, amount, true, null, referenceNumber);

        return referenceNumber;
    }

    @Override
    public BigDecimal balanceInquiry(String accountNumber, String pin) {
        Optional<AccountDto> accountDto = bookkeeping.findAccount(accountNumber);

        if (!accountDto.isPresent()) {
            throw new InvalidAccountException(accountNumber);
        }

        Account account = bankMapper.toAccount(accountDto.get());
        account.checkPin(pin);
        return account.getBalance();
    }

    private void doTransaction(String accountNumber, BigDecimal amount, boolean skipPin, String pin, String referenceNumber) {
        Optional<AccountDto> accountDto = bookkeeping.findAccount(accountNumber);

        if (!accountDto.isPresent()) {
            throw new InvalidAccountException(accountNumber);
        }

        Account account = bankMapper.toAccount(accountDto.get());
        synchronized (account) {
            Transaction transaction = new Transaction(referenceNumber, amount);
            account.performTransaction(transaction, skipPin ? account.getPin() : pin);
            bookkeeping.registerNewTransaction(accountNumber, transaction.toDto());
        }
    }

}
