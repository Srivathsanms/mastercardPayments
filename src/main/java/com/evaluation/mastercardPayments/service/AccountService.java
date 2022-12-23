package com.evaluation.mastercardPayments.service;

import java.util.List;

import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.dto.AccountRequestDto;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.model.MiniStatement;
import com.evaluation.mastercardPayments.model.TransferRequestDto;
import com.evaluation.mastercardPayments.entity.TransactionEntity;

import org.springframework.http.ResponseEntity;

public interface AccountService {
    void createAccount(AccountRequestDto accountRequestDto) throws CustomException;


    AccountEntity getAccountDetails(String accountId) throws CustomException;

    List<AccountEntity> getAllAccountDetails() throws CustomException;

   // TransactionEntity transferMoney(TransferRequestDto paymentTransferRequest) throws CustomException;

    List<MiniStatement> getMiniStatement(String accountId) throws CustomException;

    AccountEntity deleteAccount(AccountRequestDto account) throws CustomException;
}
