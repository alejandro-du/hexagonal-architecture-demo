package com.example.bank.infrastructure.jpa;

import com.example.bank.port.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    private Long id;

    private String number;

    private String pin;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<TransactionEntity> transactions;

    public AccountDto toDto() {
        return new AccountDto(number, pin, transactions.stream().map(TransactionEntity::toDto).collect(Collectors.toList()));
    }

}
