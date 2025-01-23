package com.pedrofonunes.database.account.infra.repository;

import com.pedrofonunes.database.account.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
