package com.pedrofonunes.database.account.domain.service;

import com.pedrofonunes.database.account.domain.entity.Account;
import com.pedrofonunes.database.account.domain.entity.Transaction;

import java.util.Optional;

public interface AccountService {

    Account createAccount(Account account);
    Transaction makeTransaction(Long accountId, String type, Double amount);
    Optional<Account> getAccount(Long accountId);

}
