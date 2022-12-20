package com.evaluation.mastercardPayments.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FundDto {
    private String accountNumber;
    private BigDecimal amount;
}
