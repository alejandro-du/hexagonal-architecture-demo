package com.example.bank.infrastructure.jpa;

import com.example.bank.port.dto.AccountDto;
import com.example.bank.port.dto.TransactionDto;
import com.example.bank.port.outbound.BookkeepingPort;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class JpaBookkeepingAdapter implements BookkeepingPort {

    private final AccountRepository accountRepository;

    private final EntityMapper entityMapper;

    public JpaBookkeepingAdapter(AccountRepository accountRepository, EntityMapper entityMapper) {
        this.accountRepository = accountRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public String getNewAccountNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    @Override
    public String getNewTransactionReferenceNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    @Override
    public void registerNewAccount(AccountDto accountDto) {
        AccountEntity account = entityMapper.toAccountEntity(accountDto);
        accountRepository.save(account);
    }

    @Override
    public void registerNewTransaction(String accountNumber, TransactionDto transactionDto) {
        AccountEntity account = accountRepository.findByNumber(accountNumber);
        TransactionEntity transaction = entityMapper.toTransactionEntity(transactionDto);
        account.getTransactions().add(transaction);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public Optional<AccountDto> findAccount(String number) {
        AccountEntity account = accountRepository.findByNumber(number);
        return account == null ? Optional.empty() : Optional.of(account.toDto());
    }

}
