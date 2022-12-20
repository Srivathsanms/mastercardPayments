package com.evaluation.mastercardPayments.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.entity.Currency;
import com.evaluation.mastercardPayments.entity.DataMap;

import com.evaluation.mastercardPayments.entity.TransactionEntity;

import com.evaluation.mastercardPayments.exception.CustomErrors;
import com.evaluation.mastercardPayments.exception.CustomException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private DataMap dataMap;

    @Override
    public List<TransactionEntity> getMiniStatement(String accountId) throws CustomException {
        dataMap.isValidAccount(accountId);
        return dataMap.getMiniStatement(accountId);
    }

    @Override
    public String addFundToAccount(String accountId, BigDecimal amount) throws CustomException {
        dataMap.isValidAccount(accountId);
        if (checkAccountExist(accountId)) {
            AccountEntity accManipulation = dataMap.getAccountDetails(accountId);
            accManipulation.setBalance(accManipulation.getBalance().add(amount));
            dataMap.updateDataMap(accManipulation);
            dataMap.updateTransactionHistory(prepareTransactionEntity(accManipulation,amount,TransactionEntity.TypeEnum.DEBIT));
        } else {
            throw new CustomException(CustomErrors.INVALID_ACCOUNT_NUMBER);
        }
        //Can be given as ResponseEntity, but dont know whether this is expected
        return "Account updated";
    }

    @Override
    public String fundTransfer(String debtor, String creditor, BigDecimal amount) throws CustomException {
        //Write in Transaction map as well
        dataMap.isValidAccount(debtor);
        dataMap.isValidAccount(creditor);
        if(checkAccountExist(debtor) && checkAccountExist(creditor)) {
            AccountEntity fromAccount = dataMap.getAccountDetails(debtor);
            AccountEntity toAccount = dataMap.getAccountDetails(creditor);
            if(checkBalance(fromAccount.getBalance(),amount)){
            toAccount.setBalance(toAccount.getBalance().add(amount));
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            dataMap.updateDataMap(toAccount);
            dataMap.updateDataMap(fromAccount);
            updateTransactionHistory(fromAccount,toAccount,amount);
            }else{
                throw new CustomException(CustomErrors.INSUFFICIENT_FUNDS_AVAILABLE);
            }
        }
        else {
            throw new CustomException(CustomErrors.INVALID_ACCOUNT_DETAILS);
        }

        return "Money transferred";
    }

    private void updateTransactionHistory(AccountEntity fromAccount, AccountEntity toAccount, BigDecimal amount) {
        dataMap.updateTransactionHistory(prepareTransactionEntity(fromAccount, amount,TransactionEntity.TypeEnum.DEBIT));
        dataMap.updateTransactionHistory(prepareTransactionEntity(toAccount, amount,TransactionEntity.TypeEnum.CREDIT));

    }

     private TransactionEntity prepareTransactionEntity(AccountEntity fromAccount, BigDecimal amount, TransactionEntity.TypeEnum type) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccountNumber(fromAccount.getAccountNumber());
        transactionEntity.setTransactionTime(LocalDateTime.now());
        transactionEntity.setCurrency(Currency.GBP);
        transactionEntity.setType(type);
        transactionEntity.setAmount(amount);
        return transactionEntity;
    }

    private boolean checkBalance(BigDecimal fromAccountBalance, BigDecimal balance) {
        return fromAccountBalance.subtract(balance).compareTo(BigDecimal.ZERO) >= 0;
    }

    private boolean checkAccountExist(String accountId) {
        return dataMap.getAccount(accountId);
    }


}
