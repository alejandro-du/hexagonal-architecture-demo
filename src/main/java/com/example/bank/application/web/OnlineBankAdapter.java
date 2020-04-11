package com.example.bank.application.web;

import com.example.bank.port.inbound.OnlineBankingPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OnlineBankAdapter {

    private final OnlineBankingPort onlineBanking;

    public OnlineBankAdapter(OnlineBankingPort onlineBanking) {
        this.onlineBanking = onlineBanking;
    }

    public String transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String pin) {
        return onlineBanking.transfer(fromAccountNumber, toAccountNumber, amount, pin);
    }

    public BigDecimal balanceInquiry(String accountNumber, String pin) {
        return onlineBanking.balanceInquiry(accountNumber, pin);
    }

}
