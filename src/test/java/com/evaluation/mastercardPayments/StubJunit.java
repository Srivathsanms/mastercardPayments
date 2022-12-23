package com.evaluation.mastercardPayments;

import com.evaluation.mastercardPayments.dto.AccountRequestDto;
import com.evaluation.mastercardPayments.dto.AddAmountDto;
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

public class StubJunit {

    public static AccountRequestDto getAccountRequest() {
        return new AccountRequestDto().builder()
                .accountId("111")
                .balance(new BigDecimal(1000))
                .currency(CurrencyType.GBP.name())
                .build();
    }

    public static Optional<AccountEntity> getAccountInfo() {
        AccountEntity accountEntity = new AccountEntity().builder()
                .id("111")
                .balance(new BigDecimal("1000"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        return Optional.of(accountEntity);
    }

    public static Optional<AccountEntity> getInactiveAccount() {
        AccountEntity accountEntity = new AccountEntity().builder()
                .id("111")
                .balance(new BigDecimal("1000"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.INACTIVE)
                .build();
        return Optional.of(accountEntity);
    }

    public static Optional<AccountEntity> getAccountInfo2() {
        AccountEntity accountEntity = new AccountEntity().builder()
                .id("222")
                .balance(new BigDecimal("1000"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        return Optional.of(accountEntity);
    }

    public static Optional<AccountEntity> getInactiveAccountInfo2() {
        AccountEntity accountEntity = new AccountEntity().builder()
                .id("222")
                .balance(new BigDecimal("1000"))
                .currencyType(CurrencyType.GBP)
                .accountStatus(AccountStatus.INACTIVE)
                .build();
        return Optional.of(accountEntity);
    }

    public static List<AccountEntity> getAllAccountsDetails() {
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(getAccountInfo().get());
        accounts.add(getAccountInfo2().get());
        return accounts;
    }

    public static TransferRequestDto getTransferRequest() {
        TransferRequestDto transferRequestDto = new TransferRequestDto().builder()
                .debtorAccount("111")
                .creditorAccount("222")
                .amount(new BigDecimal(20))
                .currency(CurrencyType.GBP.name())
                .build();

        return transferRequestDto;
    }

    public static List<TransactionEntity> getTransactionList() {
        List<TransactionEntity> transactionList = new ArrayList<>();

        transactionList.add(new TransactionEntity().builder()
                .currencyType(CurrencyType.GBP)
                .debtorAccount("111")
                .creditorAccount("222")
                .txAmount(new BigDecimal(20))
                .localDateTime(LocalDateTime.now())
                .build());
        transactionList.add(
                new TransactionEntity().builder()
                        .currencyType(CurrencyType.GBP)
                        .debtorAccount("111")
                        .creditorAccount("222")
                        .txAmount(new BigDecimal(30))
                        .localDateTime(LocalDateTime.now())
                        .build());

        transactionList.add(
                new TransactionEntity().builder()
                        .currencyType(CurrencyType.GBP)
                        .debtorAccount("222")
                        .creditorAccount("111")
                        .txAmount(new BigDecimal(50))
                        .localDateTime(LocalDateTime.now())
                        .build());

        return transactionList;
    }

    public static AddAmountDto getAddAmountRequest() {
        return new AddAmountDto().builder()
                .accountId("111")
                .amount(new BigDecimal(50))
                .build();
    }
}
