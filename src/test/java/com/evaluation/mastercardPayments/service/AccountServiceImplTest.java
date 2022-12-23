package com.evaluation.mastercardPayments.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.evaluation.mastercardPayments.StubJunit;

import com.evaluation.mastercardPayments.dto.AccountRequestDto;
import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.model.AccountStatus;
import com.evaluation.mastercardPayments.model.CurrencyType;
import com.evaluation.mastercardPayments.model.MiniStatement;
import com.evaluation.mastercardPayments.model.TransactionType;
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

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAccountDetailsSuccessTest() throws CustomException {
        when(accountRepository.findById(Mockito.anyString())).thenReturn(StubJunit.getAccountInfo());

        AccountEntity account = accountService.getAccountDetails("111");

        Assertions.assertEquals(account.getId(), "111");
        Assertions.assertEquals(account.getBalance(), new BigDecimal(1000));
        Assertions.assertEquals(account.getCurrencyType(), CurrencyType.GBP);
        Assertions.assertEquals(account.getAccountStatus(), AccountStatus.ACTIVE);
    }

    @Test
    void createAccountSuccessTest() throws CustomException {
        when(accountRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        accountService.createAccount(StubJunit.getAccountRequest());
        verify(accountRepository, times(1))
                .save(new AccountEntity("111", new BigDecimal(0), CurrencyType.GBP, AccountStatus.ACTIVE));
    }

    @Test
    void createAccountIfAlreadyExistsFailureTest() {
        when(accountRepository.findById(Mockito.anyString())).thenReturn(StubJunit.getAccountInfo());
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.createAccount(StubJunit.getAccountRequest());
        }, "Custom exception is expected");

        Assertions.assertEquals("Account already exists", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void getAccountDetailsForNonExistingAccountFailureTest() {
        Optional<AccountEntity> emptyAccount = Optional.empty();
        when(accountRepository.findById(Mockito.anyString())).thenReturn(emptyAccount);
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.getAccountDetails("111");
        }, "Custom exception is expected");

        Assertions.assertEquals("Invalid Account Number", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void getAllAccountDetailsSuccessTest() throws CustomException {
        when(accountRepository.findAll()).thenReturn(StubJunit.getAllAccountsDetails());
        List<AccountEntity> accountList = accountService.getAllAccountDetails();

        Assertions.assertEquals(accountList.get(0).getId(), "111");
        Assertions.assertEquals(accountList.get(0).getBalance(), new BigDecimal(1000));
        Assertions.assertEquals(accountList.get(0).getCurrencyType(), CurrencyType.GBP);
        Assertions.assertEquals(accountList.get(0).getAccountStatus(), AccountStatus.ACTIVE);

        Assertions.assertEquals(accountList.get(1).getId(), "222");
        Assertions.assertEquals(accountList.get(1).getBalance(), new BigDecimal(1000));
        Assertions.assertEquals(accountList.get(1).getCurrencyType(), CurrencyType.GBP);
        Assertions.assertEquals(accountList.get(1).getAccountStatus(), AccountStatus.ACTIVE);
    }

    @Test
    void getAllAccountDetailsWhenNonePresentFailureTest() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.getAllAccountDetails();
        }, "Custom exception is expected");

        Assertions.assertEquals("No Account Present", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void deleteAccountSuccessTest() throws CustomException {
        when(accountRepository.findById("222")).thenReturn(StubJunit.getAccountInfo2());
        AccountEntity account = accountService.deleteAccount(new AccountRequestDto().builder().accountId("222").build());

        Assertions.assertEquals(account.getAccountStatus(), AccountStatus.INACTIVE);
    }

    @Test
    void donotDeleteAccountWhenAccountNotPresentTest() throws CustomException {
        when(accountRepository.findById("222")).thenReturn(Optional.empty());
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            accountService.deleteAccount(new AccountRequestDto().builder().accountId("222").build());
        }, "Custom exception is expected");
        Assertions.assertEquals("No Account Present", customException.getCustomErrors().getErrorMessage());
    }

    @Test
    void getMiniStatementForAccountIdSuccessTest() throws CustomException {
        when(accountRepository.findById("111")).thenReturn(StubJunit.getAccountInfo());
        when(transactionDetailsRepository.findTransactionsForAccount(111)).thenReturn(StubJunit.getTransactionList());

        List<MiniStatement> miniStatement = accountService.getMiniStatement("111");

        Assertions.assertEquals(miniStatement.size(), 3);

        Assertions.assertEquals(miniStatement.get(0).getAccountId(), "222");
        Assertions.assertEquals(miniStatement.get(0).getCurrency(), CurrencyType.GBP);
        Assertions.assertEquals(miniStatement.get(0).getTransactionAmount(), new BigDecimal(20));
        Assertions.assertEquals(miniStatement.get(0).getTransactionType(), TransactionType.DEBIT);

        Assertions.assertEquals(miniStatement.get(1).getAccountId(), "222");
        Assertions.assertEquals(miniStatement.get(1).getCurrency(), CurrencyType.GBP);
        Assertions.assertEquals(miniStatement.get(1).getTransactionAmount(), new BigDecimal(30));
        Assertions.assertEquals(miniStatement.get(1).getTransactionType(), TransactionType.DEBIT);

        Assertions.assertEquals(miniStatement.get(2).getAccountId(), "222");
        Assertions.assertEquals(miniStatement.get(2).getCurrency(), CurrencyType.GBP);
        Assertions.assertEquals(miniStatement.get(2).getTransactionAmount(), new BigDecimal(50));
        Assertions.assertEquals(miniStatement.get(2).getTransactionType(), TransactionType.CREDIT);
    }

    @Test
    void getMiniStatementForAccountWithEmptyTransactionsTest() throws CustomException {
        when(accountRepository.findById("111")).thenReturn(StubJunit.getAccountInfo());
        when(transactionDetailsRepository.findTransactionsForAccount(111)).thenReturn(Collections.emptyList());

        List<MiniStatement> miniStatement = accountService.getMiniStatement("111");
        Assertions.assertEquals(miniStatement.size(), 0);
    }
}