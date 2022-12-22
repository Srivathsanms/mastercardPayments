package com.evaluation.mastercardPayments.repository;

import java.util.List;

import com.evaluation.mastercardPayments.entity.TransactionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    @Query(value = "select * from transaction_entity where debtor_account=:accountId or creditor_account=:accountId order by id desc limit 20", nativeQuery = true)
    List<TransactionEntity> findTransactionsForAccount(@Param("accountId") int accountId);
}