package com.evaluation.mastercardPayments.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransferFundDto {
    private String     debtorAccountNumber;
    private String     creditorAccountNumber;
    private BigDecimal amount;
}
