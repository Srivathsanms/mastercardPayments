package com.evaluation.mastercardPayments.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiniStatement {
    private String accountId;
    private BigDecimal transactionAmount;
    private CurrencyType currency;
    private TransactionType transactionType;
    private LocalDateTime transactionTime;
}
