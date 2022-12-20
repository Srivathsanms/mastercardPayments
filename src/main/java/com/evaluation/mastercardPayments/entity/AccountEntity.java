package com.evaluation.mastercardPayments.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
    private String accountNumber;
    private BigDecimal balance; // can be changed to BigDecimal
    private Currency currency;
    private AccountStatus accountStatus;

}

