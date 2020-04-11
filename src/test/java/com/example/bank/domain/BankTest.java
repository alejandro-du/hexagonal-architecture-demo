package com.example.bank.domain;

import com.example.bank.domain.util.BankMapper;
import com.example.bank.port.dto.AccountDto;
import com.example.bank.port.inbound.exception.InvalidAccountException;
import com.example.bank.port.inbound.exception.InvalidAmountException;
import com.example.bank.port.outbound.BookkeepingPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

public class BankTest {

    private static final String SOME_ACCOUNT_NUMBER = "account-number";
    private static final String SOME_PIN = "0000";
    private static final String SOME_REFERENCE_NUMBER = "reference-number";

    @Mock
    private BookkeepingPort bookkeeping;

    @Mock
    private BankMapper bankMapper;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldOpenAccountWithCorrectNumber() {
        String expectedAccountNumber = SOME_ACCOUNT_NUMBER;
        when(bookkeeping.getNewAccountNumber()).thenReturn(expectedAccountNumber);

        Bank bank = new Bank(bookkeeping, bankMapper);
        assertThat(bank.openNewAccount(SOME_PIN)).isEqualTo(expectedAccountNumber);
    }

    @Test
    public void shouldThrowExceptionOnNegativeDepositAmount() {
        when(bookkeeping.findAccount(SOME_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(new AccountDto(SOME_ACCOUNT_NUMBER, SOME_PIN, new ArrayList<>())));
        Bank bank = new Bank(bookkeeping, bankMapper);

        assertThatExceptionOfType(InvalidAmountException.class)
                .isThrownBy(() -> bank.deposit(SOME_ACCOUNT_NUMBER, new BigDecimal(-777)));
    }

    @Test
    public void shouldThrowExceptionOnNegativeWithdrawalAmount() {
        when(bookkeeping.findAccount(SOME_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(new AccountDto(SOME_ACCOUNT_NUMBER, SOME_PIN, new ArrayList<>())));
        Bank bank = new Bank(bookkeeping, bankMapper);

        assertThatExceptionOfType(InvalidAmountException.class)
                .isThrownBy(() -> bank.withdraw(SOME_ACCOUNT_NUMBER, new BigDecimal(-777), SOME_PIN));
    }

    @Test
    public void shouldThrowExceptionOnNegativeTransferAmount() {
        Bank bank = new Bank(bookkeeping, bankMapper);

        assertThatExceptionOfType(InvalidAmountException.class)
                .isThrownBy(() -> bank.transfer(SOME_ACCOUNT_NUMBER, SOME_ACCOUNT_NUMBER + "1", new BigDecimal(-777), SOME_PIN));
    }

    @Test
    public void shouldThrowExceptionWhenSourceAndDestinationAccountsAreTheSame() {
        Bank bank = new Bank(bookkeeping, bankMapper);

        assertThatExceptionOfType(InvalidAccountException.class)
                .isThrownBy(() -> bank.transfer(SOME_ACCOUNT_NUMBER, SOME_ACCOUNT_NUMBER, new BigDecimal(777), SOME_PIN));
    }

    @Test
    public void accountShouldHaveCorrectBalanceAfterDeposits() {
        Account account = new Account(SOME_ACCOUNT_NUMBER, SOME_PIN);
        AccountDto accountDto = account.toDto();
        when(bankMapper.toAccount(accountDto)).thenReturn(account);
        when(bookkeeping.findAccount(SOME_ACCOUNT_NUMBER)).thenReturn(Optional.of(accountDto));
        when(bookkeeping.getNewTransactionReferenceNumber()).thenReturn(SOME_REFERENCE_NUMBER);
        Bank bank = new Bank(bookkeeping, bankMapper);

        bank.deposit(SOME_ACCOUNT_NUMBER, new BigDecimal(2));
        bank.deposit(SOME_ACCOUNT_NUMBER, new BigDecimal(3));
        assertThat(account.getBalance(SOME_PIN)).isEqualTo(new BigDecimal(5));
    }

    @Test
    public void accountShouldHaveCorrectBalanceAfterWithdrawals() {
        List<Transaction> transactions = Stream.of(new Transaction(SOME_REFERENCE_NUMBER, new BigDecimal(10))).collect(Collectors.toList());
        Account account = new Account(SOME_ACCOUNT_NUMBER, SOME_PIN, transactions);
        AccountDto accountDto = account.toDto();
        when(bankMapper.toAccount(accountDto)).thenReturn(account);
        when(bookkeeping.findAccount(SOME_ACCOUNT_NUMBER)).thenReturn(Optional.of(accountDto));
        when(bookkeeping.getNewTransactionReferenceNumber()).thenReturn(SOME_REFERENCE_NUMBER);
        Bank bank = new Bank(bookkeeping, bankMapper);

        bank.withdraw(SOME_ACCOUNT_NUMBER, new BigDecimal(2), SOME_PIN);
        bank.withdraw(SOME_ACCOUNT_NUMBER, new BigDecimal(3), SOME_PIN);
        assertThat(account.getBalance(SOME_PIN)).isEqualTo(new BigDecimal(5));
    }

    @Test
    public void accountsShouldHaveCorrectBalanceAfterTransfer() {
        String accountNumber1 = SOME_ACCOUNT_NUMBER + "1";
        String accountNumber2 = SOME_ACCOUNT_NUMBER + "2";
        List<Transaction> transactions1 = Stream.of(new Transaction(SOME_REFERENCE_NUMBER, new BigDecimal(10))).collect(Collectors.toList());
        List<Transaction> transactions2 = Stream.of(new Transaction(SOME_REFERENCE_NUMBER, new BigDecimal(5))).collect(Collectors.toList());
        Account account1 = new Account(accountNumber1, SOME_PIN, transactions1);
        Account account2 = new Account(accountNumber2, SOME_PIN, transactions2);
        AccountDto accountDto1 = account1.toDto();
        AccountDto accountDto2 = account2.toDto();
        when(bankMapper.toAccount(accountDto1)).thenReturn(account1);
        when(bankMapper.toAccount(accountDto2)).thenReturn(account2);
        when(bookkeeping.findAccount(accountNumber1)).thenReturn(Optional.of(accountDto1));
        when(bookkeeping.findAccount(accountNumber2)).thenReturn(Optional.of(accountDto2));
        when(bookkeeping.getNewTransactionReferenceNumber()).thenReturn(SOME_REFERENCE_NUMBER);
        Bank bank = new Bank(bookkeeping, bankMapper);

        bank.transfer(accountNumber1, accountNumber2, new BigDecimal(3), SOME_PIN);
        assertThat(account1.getBalance(SOME_PIN)).isEqualTo(new BigDecimal(7));
        assertThat(account2.getBalance(SOME_PIN)).isEqualTo(new BigDecimal(8));
    }

}
