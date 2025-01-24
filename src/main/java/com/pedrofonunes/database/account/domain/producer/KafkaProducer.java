package com.pedrofonunes.database.account.domain.producer;

import com.pedrofonunes.database.account.domain.entity.Account;
import com.pedrofonunes.database.account.domain.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendAccountDetails(Account account) {
        String topic = "bank-account-topic";
        String message = String.format("Account Created: %s - %s - %.2f",
                account.getAccountNumber(), account.getOwnerName(), account.getBalance());
        kafkaTemplate.send(topic, message);
    }

    public void sendTransactionDetails(Transaction transaction) {
        String topic = "bank-transaction-topic";
        String message = String.format("Transaction: %s - %.2f - %s - %s",
                transaction.getType(), transaction.getAmount(), transaction.getAccount().getAccountNumber(),
                transaction.getTimestamp());
        kafkaTemplate.send(topic, message);
    }

}
