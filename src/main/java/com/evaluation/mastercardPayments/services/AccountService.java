package com.evaluation.mastercardPayments.services;

import java.util.List;

import com.evaluation.mastercardPayments.entity.AccountEntity;

import com.evaluation.mastercardPayments.exception.CustomException;

import org.springframework.http.ResponseEntity;


public interface AccountService {

   public AccountEntity getBalance(String accountId) throws CustomException;
   public ResponseEntity<String> createAccount(String accountId) throws CustomException;
   public String deleteAccount(String accountId) throws CustomException;
   List<AccountEntity> fetchAllAccounts();
}
