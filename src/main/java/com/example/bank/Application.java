package com.example.bank;

import com.example.bank.infrastructure.jpa.AccountEntity;
import com.example.bank.infrastructure.jpa.AccountRepository;
import com.example.bank.infrastructure.jpa.TransactionEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demoData(AccountRepository accountRepository) {
        return args -> {
            Set<TransactionEntity> transactions = Stream.of(new TransactionEntity(null, "1", new BigDecimal(1000))).collect(Collectors.toSet());
            accountRepository.save(new AccountEntity(null, "111", "111", transactions));
            accountRepository.save(new AccountEntity(null, "222", "222", new HashSet<>()));
            accountRepository.save(new AccountEntity(null, "333", "333", new HashSet<>()));
        };
    }

}
