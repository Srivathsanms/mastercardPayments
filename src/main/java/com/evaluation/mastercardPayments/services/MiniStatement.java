package com.evaluation.mastercardPayments.services;

import java.util.List;


import com.evaluation.mastercardPayments.entity.TransactionEntity;
import com.evaluation.mastercardPayments.exception.CustomException;

public interface MiniStatement {
    public List<TransactionEntity> getMiniStatement(String accountId) throws CustomException;
}
