package com.evaluation.mastercardPayments.service;

import com.evaluation.mastercardPayments.dto.AddAmountDto;
import com.evaluation.mastercardPayments.entity.TransactionEntity;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.model.TransferRequestDto;

public interface TransactionService {

    void addAmount(AddAmountDto addAmountDto) throws CustomException;
    TransactionEntity transferAmount(TransferRequestDto paymentTransferRequest) throws CustomException;
}
