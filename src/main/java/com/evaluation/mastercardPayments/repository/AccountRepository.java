package com.evaluation.mastercardPayments.repository;

import com.evaluation.mastercardPayments.entity.AccountEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {

}
