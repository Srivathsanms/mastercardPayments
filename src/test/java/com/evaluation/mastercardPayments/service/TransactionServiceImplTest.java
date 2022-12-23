package com.evaluation.mastercardPayments.service;

import com.evaluation.mastercardPayments.StubJunit;
import com.evaluation.mastercardPayments.dto.AddAmountDto;
import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.entity.TransactionEntity;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.model.AccountStatus;
import com.evaluation.mastercardPayments.model.CurrencyType;
import com.evaluation.mastercardPayments.model.TransferRequestDto;
import com.evaluation.mastercardPayments.repository.AccountRepository;
import com.evaluation.mastercardPayments.repository.TransactionRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionServiceImplTest {

    @MockBean
    private TransactionRepository transactionDetailsRepository;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Test
    void transferMoneySuccessTest() throws CustomException {
        TransferRequestDto paymentTransferRequest = StubJunit.getTransferRequest();
        when(accountRepository.findById("111")).thenReturn(StubJunit.getAccountInfo());
        when(accountRepository.findById("222")).thenReturn(StubJunit.getAccountInfo2());
        when(accountRepository.save(Mockito.any(AccountEntity.class))).thenReturn(null);
        when(transactionDetailsRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(null);

        TransactionEntity transactionDetails = transactionService.transferAmount(paymentTransferRequest);

        Assertions.assertEquals(transactionDetails.getDebtorAccount(), "111");
        Assertions.assertEquals(transactionDetails.getCreditorAccount(), "222");
        Assertions.assertEquals(transactionDetails.getTxAmount(), new BigDecimal(20));
        Assertions.assertEquals(transactionDetails.getCurrencyType(), CurrencyType.GBP);
    }

    @Test
    void transferMoneyWhenSenderNotPresentFailureTest() {
        TransferRequestDto paymentTransferRequest = StubJunit.getTransferRequest();
        Optional<AccountEntity> emptyAccount = Optional.empty();
        when(accountRepository.findById("111")).thenReturn(emptyAccount);
        when(accountRepository.findById("222")).thenReturn(StubJunit.getAccountInfo2());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            transactionService.transferAmount(paymentTransferRequest);
        }, "Custom exception is expected");

        Assertions.assertEquals("Invalid Debtor account", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void donotTransferFundsWhenTheAmountToTransferIsHigherThanDebtorBalance() throws CustomException {
        TransferRequestDto paymentTransferRequest = new TransferRequestDto().builder()
                .debtorAccount("111")
                .creditorAccount("222")
                .amount(new BigDecimal(1500))
                .currency(CurrencyType.GBP.name())
                .build();
        when(accountRepository.findById("111")).thenReturn(StubJunit.getAccountInfo());
        when(accountRepository.findById("222")).thenReturn(StubJunit.getAccountInfo2());
        when(accountRepository.save(Mockito.any(AccountEntity.class))).thenReturn(null);
        when(transactionDetailsRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(null);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            transactionService.transferAmount(paymentTransferRequest);
        }, "Custom exception is expected");

        Assertions.assertEquals("Insufficient funds available", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void transferMoneyWhenDebtorIsInactiveFailureTest() {
        TransferRequestDto paymentTransferRequest = StubJunit.getTransferRequest();
        when(accountRepository.findById("111")).thenReturn(StubJunit.getInactiveAccount());
        when(accountRepository.findById("222")).thenReturn(StubJunit.getAccountInfo2());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            transactionService.transferAmount(paymentTransferRequest);
        }, "Custom exception is expected");

        Assertions.assertEquals("Debtor account not active", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void transferMoneyWhenCreditorIsInactiveFailureTest() {
        TransferRequestDto paymentTransferRequest = StubJunit.getTransferRequest();
        when(accountRepository.findById("111")).thenReturn(StubJunit.getAccountInfo());
        when(accountRepository.findById("222")).thenReturn(StubJunit.getInactiveAccountInfo2());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            transactionService.transferAmount(paymentTransferRequest);
        }, "Custom exception is expected");

        Assertions.assertEquals("Creditor account not active", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void transferMoneyWhenCreditorIsNotPresentFailureTest() {
        TransferRequestDto paymentTransferRequest = StubJunit.getTransferRequest();
        when(accountRepository.findById("111")).thenReturn(StubJunit.getAccountInfo());
        when(accountRepository.findById("222")).thenReturn(Optional.empty());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            transactionService.transferAmount(paymentTransferRequest);
        }, "Custom exception is expected");

        Assertions.assertEquals("Invalid creditor account", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void addFundsToTheGivenAccountNumberTest() throws CustomException {
        AddAmountDto addAmountRequest = StubJunit.getAddAmountRequest();
        when(accountRepository.findById("111")).thenReturn(StubJunit.getAccountInfo());

        transactionService.addAmount(addAmountRequest);
        verify(accountRepository, times(1))
                .save(new AccountEntity("111", new BigDecimal(1050), CurrencyType.GBP, AccountStatus.ACTIVE));
    }

    @Test
    void addFundsToAccountNumberThatDoesnotExistTest() throws CustomException {
        AddAmountDto addAmountRequest = StubJunit.getAddAmountRequest();
        when(accountRepository.findById("111")).thenReturn(Optional.empty());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            transactionService.addAmount(addAmountRequest);
        }, "Custom exception is expected");

        Assertions.assertEquals("Invalid Account Number", customException.getCustomErrors().getErrorMessage());

    }
}