package com.pedrofonunes.database.account.domain.service.impl;

import com.pedrofonunes.database.account.domain.entity.Account;
import com.pedrofonunes.database.account.domain.entity.Transaction;
import com.pedrofonunes.database.account.domain.producer.KafkaProducer;
import com.pedrofonunes.database.account.domain.service.AccountService;
import com.pedrofonunes.database.account.infra.repository.AccountRepository;
import com.pedrofonunes.database.account.infra.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaProducer kafkaProducer;

    @Override
    public Account createAccount(Account account) {
        Account savedAccount = accountRepository.save(account);

        // Enviar a nova conta para o Kafka
        kafkaProducer.sendAccountDetails(savedAccount);
        return savedAccount;
    }

    @Override
    public Transaction makeTransaction(Long accountId, String type, Double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if ("WITHDRAW".equalsIgnoreCase(type)) {
            if (account.getBalance() < amount) {
                throw new RuntimeException("Insufficient balance");
            }
            account.setBalance(account.getBalance() - amount);
        } else if ("DEPOSIT".equalsIgnoreCase(type)) {
            account.setBalance(account.getBalance() + amount);
        } else {
            throw new RuntimeException("Invalid transaction type");
        }

        // Criar e salvar a transação
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(type.toUpperCase());
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Atualizar a conta e enviar as mudanças para o Kafka
        accountRepository.save(account);
        kafkaProducer.sendTransactionDetails(transaction);

        return transaction;
    }

    @Override
    public Optional<Account> getAccount(Long accountId) {
        return accountRepository.findById(accountId);
    }
}
