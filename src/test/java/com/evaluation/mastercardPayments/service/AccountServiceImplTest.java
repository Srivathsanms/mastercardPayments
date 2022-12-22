package com.evaluation.mastercardPayments.service;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.evaluation.mastercardPayments.TestSupportUtils;

import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.entity.TransactionEntity;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.model.AccountStatus;
import com.evaluation.mastercardPayments.model.CurrencyType;
import com.evaluation.mastercardPayments.model.MiniStatement;
import com.evaluation.mastercardPayments.model.TransactionType;
import com.evaluation.mastercardPayments.model.TransferRequestDto;
import com.evaluation.mastercardPayments.repository.AccountRepository;

import com.evaluation.mastercardPayments.repository.TransactionRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountServiceImplTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionDetailsRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createAccountIfAlreadyExists_failure() {
        when(accountRepository.findById(Mockito.anyString())).thenReturn(TestSupportUtils.getOptionalAccountInfo());
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.createAccount(TestSupportUtils.getAccountInfoRequest());
        }, "Custom exception is expected");

        Assertions.assertEquals("Account already exists with Account Id 111", customException.getCustomErrors().getErrorMessage());
    }

    /*Fetch account details for an non existing account*/
    @Test
    void getAccountDetails_failure() {
        Optional<AccountEntity> emptyAccount = Optional.empty();
        when(accountRepository.findById(Mockito.anyString())).thenReturn(emptyAccount);
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.getAccountDetails("111");
        }, "Custom exception is expected");

        Assertions.assertEquals("Account : 111 does not exists", customException.getCustomErrors().getErrorMessage());
    }

    /*Fetch account details for an non existing account*/
    @Test
    void getAccountDetails_success() throws CustomException {
        when(accountRepository.findById(Mockito.anyString())).thenReturn(TestSupportUtils.getOptionalAccountInfo());

        AccountEntity account = accountService.getAccountDetails("111");

        Assertions.assertEquals(account.getId(), 111);
        Assertions.assertEquals(account.getBalance(), new BigDecimal(1000));
        Assertions.assertEquals(account.getCurrencyType(), CurrencyType.GBP);
        Assertions.assertEquals(account.getAccountStatus(), AccountStatus.ACTIVE);
    }

    @Test
    void getAllAccountDetails_success() throws CustomException {
        when(accountRepository.findAll()).thenReturn(TestSupportUtils.getAllAccountsDetails());
        List<AccountEntity> accountList = accountService.getAllAccountDetails();

        Assertions.assertEquals(accountList.get(0).getId(), 111);
        Assertions.assertEquals(accountList.get(0).getBalance(), new BigDecimal(1000));
        Assertions.assertEquals(accountList.get(0).getCurrencyType(), CurrencyType.GBP);
        Assertions.assertEquals(accountList.get(0).getAccountStatus(), AccountStatus.ACTIVE);

        Assertions.assertEquals(accountList.get(1).getId(), 222);
        Assertions.assertEquals(accountList.get(1).getBalance(), new BigDecimal(1000));
        Assertions.assertEquals(accountList.get(1).getCurrencyType(), CurrencyType.GBP);
        Assertions.assertEquals(accountList.get(1).getAccountStatus(), AccountStatus.ACTIVE);
    }

    @Test
    void getAllAccountDetailsWhenNonePresent_failure() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.getAllAccountDetails();
        }, "Custom exception is expected");

        Assertions.assertEquals("No Account present", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void transferMoney_success() throws CustomException {
        TransferRequestDto paymentTransferRequest = new TransferRequestDto().builder()
                .senderId("111")
                .receiverId("222")
                .amount(new BigDecimal(20))
                .currency(CurrencyType.GBP.name())
                .build();
        when(accountRepository.findById("111")).thenReturn(TestSupportUtils.getOptionalAccountInfo());
        when(accountRepository.findById("222")).thenReturn(TestSupportUtils.getOptionalAccountInfo2());
        when(accountRepository.save(Mockito.any(AccountEntity.class))).thenReturn(null);
        when(transactionDetailsRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(null);
        //Mockito.doNothing().when(transactionDetailsRepository.save(Mockito.any()));

        TransactionEntity transactionDetails = transactionService.transferMoney(paymentTransferRequest);

        Assertions.assertEquals(transactionDetails.getSenderId(), 111);
        Assertions.assertEquals(transactionDetails.getReceiverId(), 222);
        Assertions.assertEquals(transactionDetails.getTxnAmount(), new BigDecimal(20));
        Assertions.assertEquals(transactionDetails.getCurrencyType(), CurrencyType.GBP);
    }

    @Test
    void transferMoneyWhenSenderNotPresent_failure() {
        TransferRequestDto paymentTransferRequest = TestSupportUtils.getPaymentTransferRequest();
        Optional<AccountEntity> emptyAccount = Optional.empty();
        when(accountRepository.findById("111")).thenReturn(emptyAccount);
        when(accountRepository.findById("222")).thenReturn(TestSupportUtils.getOptionalAccountInfo2());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            transactionService.transferMoney(paymentTransferRequest);
        }, "Custom exception is expected");

        Assertions.assertEquals("Invalid Sender Account : 111", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void transferMoneyWhenSenderIsInactive_failure() {
        TransferRequestDto paymentTransferRequest = TestSupportUtils.getPaymentTransferRequest();
        when(accountRepository.findById("111")).thenReturn(TestSupportUtils.getOptionalInactiveAccountInfo());
        when(accountRepository.findById("222")).thenReturn(TestSupportUtils.getOptionalAccountInfo2());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            transactionService.transferMoney(paymentTransferRequest);
        }, "Custom exception is expected");

        Assertions.assertEquals("Sender Account : 111 not Active", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void getMiniStatementForAccountId111() throws CustomException {
        when(accountRepository.findById("111")).thenReturn(TestSupportUtils.getOptionalAccountInfo());
        when(transactionDetailsRepository.findTransactionsForAccount(111)).thenReturn(TestSupportUtils.getTransactionInfoList());

        List<MiniStatement> miniStatement = accountService.getMiniStatement("111");

        Assertions.assertEquals(miniStatement.size(), 3);

        Assertions.assertEquals(miniStatement.get(0).getAccountId(), 222);
        Assertions.assertEquals(miniStatement.get(0).getCurrency(), CurrencyType.GBP);
        Assertions.assertEquals(miniStatement.get(0).getTransactionAmount(), new BigDecimal(20));
        Assertions.assertEquals(miniStatement.get(0).getTransactionType(), TransactionType.DEBIT);

        Assertions.assertEquals(miniStatement.get(1).getAccountId(), 222);
        Assertions.assertEquals(miniStatement.get(1).getCurrency(), CurrencyType.GBP);
        Assertions.assertEquals(miniStatement.get(1).getTransactionAmount(), new BigDecimal(30));
        Assertions.assertEquals(miniStatement.get(1).getTransactionType(), TransactionType.DEBIT);

        Assertions.assertEquals(miniStatement.get(2).getAccountId(), 222);
        Assertions.assertEquals(miniStatement.get(2).getCurrency(), CurrencyType.GBP);
        Assertions.assertEquals(miniStatement.get(2).getTransactionAmount(), new BigDecimal(50));
        Assertions.assertEquals(miniStatement.get(2).getTransactionType(), TransactionType.CREDIT);

    }
}