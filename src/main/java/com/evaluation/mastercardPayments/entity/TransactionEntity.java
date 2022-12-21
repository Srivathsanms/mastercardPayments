package com.evaluation.mastercardPayments.entity;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.evaluation.mastercardPayments.model.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String senderId;

    private String receiverId;

    private LocalDateTime localDateTime;

    private BigDecimal txnAmount;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    private String txnType;
}
