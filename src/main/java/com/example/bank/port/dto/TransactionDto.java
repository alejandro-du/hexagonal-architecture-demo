package com.example.bank.port.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionDto implements Serializable {

    private String referenceNumber;

    private BigDecimal amount;

}
