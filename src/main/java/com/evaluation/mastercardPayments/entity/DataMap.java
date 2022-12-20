package com.evaluation.mastercardPayments.entity;

import static org.springframework.http.HttpStatus.OK;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.evaluation.mastercardPayments.exception.CustomErrors;
import com.evaluation.mastercardPayments.exception.CustomException;

import org.apache.commons.lang3.StringUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class DataMap {


    public Map<String, AccountEntity> accountMap = new HashMap<>();
    public Map<String, List<TransactionEntity>> transactionMap = new HashMap<>();

    public void addTransaction(TransactionEntity transaction) {
        List<TransactionEntity> transactions = transactionMap.get("TRANSACTION");
        transactions.add(transaction);
        transactionMap.put("TRANSACTION", transactions);
    }

    public ResponseEntity<String> insertAccount(String accountId) {
        AccountEntity account = new AccountEntity().builder()
                .accountNumber(accountId)
                .balance(BigDecimal.ZERO)
                .currency(Currency.GBP)
                .accountStatus(AccountStatus.ACTIVE).build();
        /*AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNumber(accountId);
        accountEntity.setBalance(BigDecimal.ZERO);
        accountEntity.setCurrency(Currency.GBP);
        accountEntity.setAccountStatus(AccountStatus.ACTIVE);*/
        accountMap.put(accountId, account);
        return new ResponseEntity<>("Account Created", OK);

    }

    public boolean getAccount(String accountId) {
        return this.accountMap.containsKey(accountId) && this.accountMap.get(accountId).getAccountStatus().equals(AccountStatus.ACTIVE);
    }

    public AccountEntity getAccountDetails(String accountId) throws CustomException {
        if (this.accountMap.containsKey(accountId)) {
            return this.accountMap.get(accountId);
        } else {
            throw new CustomException(CustomErrors.INVALID_ACCOUNT_NUMBER);
        }

    }

    public void updateDataMap(AccountEntity accManipulation) {
        AccountEntity manipulate = this.accountMap.get(accManipulation.getAccountNumber());
        manipulate.setBalance(accManipulation.getBalance());
       /* this.accountMap.put(accManipulation.getAccountNumber(),
                this.accountMap.get(accManipulation.getAccountNumber()).setBalance(accManipulation.getBalance()));
       */
        System.out.println("Account balance check : " + this.accountMap);
    }

    public String deleteAccount(String accountId) {
        this.accountMap.get(accountId).setAccountStatus(AccountStatus.INACTIVE);
        return "Account Deleted";
    }

    public void updateTransactionHistory(TransactionEntity transactionEntity) {
        //Dummy Check for initial entries as we are using HashMap instead of H2 or Oracle
        if (this.transactionMap.isEmpty()) {
            List<TransactionEntity> tx = new ArrayList<>();
            tx.add(transactionEntity);
            this.transactionMap.put(transactionEntity.getAccountNumber(), tx);
        } else if (!this.transactionMap.containsKey(transactionEntity.getAccountNumber())) {
            List<TransactionEntity> tx = new ArrayList<>();
            tx.add(transactionEntity);
            this.transactionMap.put(transactionEntity.getAccountNumber(), tx);
        } else {
            this.transactionMap.get(transactionEntity.getAccountNumber()).add(transactionEntity);
        }
        System.out.println("Transaction map : " + this.transactionMap);
    }

    public List<TransactionEntity> getMiniStatement(String accountId) throws CustomException {
        if (this.transactionMap.containsKey(accountId)) {
            return this.transactionMap.get(accountId).stream()
                    .sorted(Comparator.comparing(TransactionEntity::getTransactionTime).reversed())
                    .limit(20).collect(Collectors.toList());
        } else {
            throw new CustomException(CustomErrors.INVALID_ACCOUNT_NUMBER);
        }
    }

    //account number syntax validation
    public void isValidAccount(String accountId) throws CustomException {
        if (accountId == null || !StringUtils.isNumeric(accountId)) {
            throw new CustomException(CustomErrors.INVALID_ACCOUNT_NUMBER);
        }
    }
}


