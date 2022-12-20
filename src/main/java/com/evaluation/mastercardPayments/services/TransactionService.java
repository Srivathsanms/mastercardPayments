package com.evaluation.mastercardPayments.services;

import java.math.BigDecimal;
import java.util.List;


import com.evaluation.mastercardPayments.entity.TransactionEntity;
import com.evaluation.mastercardPayments.exception.CustomException;

public interface TransactionService {
    public String fundTransfer(String debtor,String creditor, BigDecimal amount) throws CustomException;
    public List<TransactionEntity> getMiniStatement(String accountId) throws CustomException;

    String addFundToAccount(String accountId, BigDecimal amount) throws CustomException;
}
