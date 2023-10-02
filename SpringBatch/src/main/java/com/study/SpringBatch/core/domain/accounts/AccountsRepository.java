package com.study.SpringBatch.core.domain.accounts;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Accounts,Integer> {
}
