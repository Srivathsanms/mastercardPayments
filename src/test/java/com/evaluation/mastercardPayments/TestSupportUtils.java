package com.evaluation.mastercardPayments;

import com.evaluation.mastercardPayments.dto.AccountRequestDto;
import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.entity.TransactionEntity;
import com.evaluation.mastercardPayments.model.AccountStatus;
import com.evaluation.mastercardPayments.model.CurrencyType;
import com.evaluation.mastercardPayments.model.TransferRequestDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class TestSupportUtils {

    public static AccountRequestDto getAccountInfoRequest() {
        return new AccountRequestDto().builder()
                .accountId("111")
                .balance(new BigDecimal(1000))
                .currency(CurrencyType.GBP.name())
                .build();
    }

    public static Optional<AccountEntity> getOptionalAccountInfo() {
        AccountEntity accountInfo = new AccountEntity().builder()
                .id("111")
                .balance(new BigDecimal("1000"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        return Optional.of(accountInfo);
    }

    public static Optional<AccountEntity> getOptionalInactiveAccountInfo() {
        AccountEntity accountInfo = new AccountEntity().builder()
                .id("111")
                .balance(new BigDecimal("1000"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.INACTIVE)
                .build();
        return Optional.of(accountInfo);
    }

    public static Optional<AccountEntity> getOptionalAccountInfo2() {
        AccountEntity accountInfo = new AccountEntity().builder()
                .id("222")
                .balance(new BigDecimal("1000"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        return Optional.of(accountInfo);
    }

    public static List<AccountEntity> getAllAccountsDetails() {
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(getOptionalAccountInfo().get());
        accounts.add(getOptionalAccountInfo2().get());
        return accounts;
    }

    public static TransferRequestDto getPaymentTransferRequest() {
        TransferRequestDto paymentTransferRequest = new TransferRequestDto().builder()
                .debtorAccount("111")
                .creditorAccount("222")
                .amount(new BigDecimal(20))
                .currency(CurrencyType.GBP.name())
                .build();

        return paymentTransferRequest;
    }

    public static List<TransactionEntity> getTransactionInfoList() {
        List<TransactionEntity> transactionInfoList = new ArrayList<>();

        transactionInfoList.add(new TransactionEntity().builder()
                .currencyType(CurrencyType.GBP)
                .senderId("111")
                .receiverId("222")
                .txnAmount(new BigDecimal(20))
                .localDateTime(LocalDateTime.now())
                .build());
        transactionInfoList.add(
                new TransactionEntity().builder()
                        .currencyType(CurrencyType.GBP)
                        .senderId("111")
                        .receiverId("222")
                        .txnAmount(new BigDecimal(30))
                        .localDateTime(LocalDateTime.now())
                        .build());

        transactionInfoList.add(
                new TransactionEntity().builder()
                        .currencyType(CurrencyType.GBP)
                        .senderId("222")
                        .receiverId("111")
                        .txnAmount(new BigDecimal(50))
                        .localDateTime(LocalDateTime.now())
                        .build());

        return transactionInfoList;
    }



}
