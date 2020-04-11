package com.example.bank.port.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class AccountDto implements Serializable {

    private String number;

    private String pin;

    private List<TransactionDto> transactions;

}
