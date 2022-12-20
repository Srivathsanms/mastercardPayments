package com.evaluation.mastercardPayments.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransactionEntity {
    private String accountNumber;
    private BigDecimal amount;
    private Currency currency;
    private TypeEnum type;
    private LocalDateTime transactionTime;

    public enum TypeEnum {
        DEBIT, CREDIT
    }

}
