package com.evaluation.mastercardPayments.services;

import java.util.List;
import java.util.stream.Collectors;

import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.entity.DataMap;

import com.evaluation.mastercardPayments.exception.CustomErrors;
import com.evaluation.mastercardPayments.exception.CustomException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private DataMap dataMap;

    @Autowired
    TransactionService transactionService;

    @Override
    public AccountEntity getBalance(String accountId) throws CustomException {
        dataMap.isValidAccount(accountId);
        return dataMap.getAccountDetails(accountId);
    }

    @Override
    public ResponseEntity<String> createAccount(String accountId) throws CustomException {
        dataMap.isValidAccount(accountId);
        if (!checkAccountExist(accountId)) return dataMap.insertAccount(accountId);
        else throw new CustomException(CustomErrors.ACCOUNT_ALREADY_EXIST);
    }


    @Override
    public String deleteAccount(String accountId) throws CustomException {
        dataMap.isValidAccount(accountId);
        if (checkAccountExist(accountId)) return dataMap.deleteAccount(accountId);
        else throw new CustomException(CustomErrors.INVALID_ACCOUNT_DETAILS);
    }


    //No Account present in the bank condition check
    @Override
    public List<AccountEntity> fetchAllAccounts() {
        return dataMap.accountMap.values().stream().collect(Collectors.toList());
    }

    private boolean checkAccountExist(String accountId) {
        return dataMap.getAccount(accountId);
    }
}
