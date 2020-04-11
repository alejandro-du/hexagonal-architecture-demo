package com.example.bank.port.inbound;

import java.math.BigDecimal;

public interface OnlineBankingPort {

    String transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String pin);

    BigDecimal balanceInquiry(String accountNumber, String pin);

}
