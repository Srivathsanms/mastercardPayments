package com.evaluation.mastercardPayments.services;

import java.util.List;

import com.evaluation.mastercardPayments.entity.DataMap;

import com.evaluation.mastercardPayments.entity.TransactionEntity;

import com.evaluation.mastercardPayments.exception.CustomException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MiniStatementImpl implements MiniStatement{

    @Autowired
    DataMap dataMap;


    @Override
    public List<TransactionEntity> getMiniStatement(String accountId) throws CustomException {
       return dataMap.getMiniStatement(accountId);
    }
}
