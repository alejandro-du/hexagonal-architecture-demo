package com.example.bank.infrastructure.jpa;

import com.example.bank.port.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransactionEntity {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    private String referenceNumber;

    private BigDecimal amount;

    public TransactionDto toDto() {
        return new TransactionDto(referenceNumber, amount);
    }

}
