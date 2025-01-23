package com.pedrofonunes.database.account.infra.repository;

import com.pedrofonunes.database.account.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
