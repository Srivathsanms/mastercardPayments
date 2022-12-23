package com.evaluation.mastercardPayments.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.evaluation.mastercardPayments.exception.CustomErrors;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.dto.AccountRequestDto;
import com.evaluation.mastercardPayments.model.AccountStatus;
import com.evaluation.mastercardPayments.model.CurrencyType;
import com.evaluation.mastercardPayments.model.MiniStatement;
import com.evaluation.mastercardPayments.entity.TransactionEntity;
import com.evaluation.mastercardPayments.model.TransactionType;
import com.evaluation.mastercardPayments.repository.AccountRepository;

import com.evaluation.mastercardPayments.repository.TransactionRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Primary
public class AccountServiceImpl implements AccountService {

    private static final Logger LOG = LogManager.getLogger(AccountServiceImpl.class);
    @Autowired
    private              AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public void createAccount(AccountRequestDto accountInfoRequest) throws CustomException {
        AccountEntity account = new AccountEntity().builder()
                .id(accountInfoRequest.getAccountId())
                .balance(BigDecimal.ZERO)
                //TODO: Small logic change required
                .currencyType(CurrencyType.valueOf(accountInfoRequest.getCurrency()))
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        Optional<AccountEntity> findAccount = accountRepository.findById(account.getId());
        if (findAccount.isPresent()) {
            LOG.info("Account already exist for Id : {}", accountInfoRequest.getAccountId());
            throw new CustomException(CustomErrors.ACCOUNT_ALREADY_EXIST);
        }
        LOG.info("Creating Account for Id : {}", accountInfoRequest.getAccountId());
        accountRepository.save(account);
    }



    public AccountEntity getAccountDetails(String accountId) throws CustomException {
        Optional<AccountEntity> accountInfo = accountRepository.findById(accountId);
        if (accountInfo.isPresent()) {
            LOG.info("Got Account Details for Id : {}", accountId);
            return accountInfo.get();
        }
        LOG.info("Fetching details for the Id :{} failed", accountId);
        throw new CustomException(CustomErrors.INVALID_ACCOUNT_NUMBER);
    }

    public List<AccountEntity> getAllAccountDetails() throws CustomException {
        List<AccountEntity> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            LOG.info("Getting all accounts failed and there are no accounts");
            throw new CustomException(CustomErrors.NO_ACCOUNT_AVAILABLE);
        }
        return accounts;
    }


    //TODO : I dont want to break the API contract provided in the example,
    // Ideally I feel that this needs to be in the Transaction end point
    //TODO : Open for discussion on this
    public List<MiniStatement> getMiniStatement(String accountId) throws CustomException {
        getAccountDetails(accountId);
        List<TransactionEntity> transactions = transactionRepository.findTransactionsForAccount(Integer.parseInt(accountId));
        List<MiniStatement> miniStatements = new ArrayList<>();
        if(transactions.isEmpty()) {
            LOG.info("Transaction is empty for Id : {}", accountId);
            return Collections.emptyList();
        }
        for (TransactionEntity tx : transactions) {
            MiniStatement miniStatement = new MiniStatement().builder()
                    .accountId(tx.getDebtorAccount().equals(accountId) ? tx.getCreditorAccount() : tx.getDebtorAccount())
                    .transactionAmount(tx.getTxAmount())
                    .currency(tx.getCurrencyType())
                    .transactionType(tx.getDebtorAccount().equals(accountId) ? TransactionType.DEBIT : TransactionType.CREDIT)
                    .transactionTime(tx.getLocalDateTime())
                    .build();
            miniStatements.add(miniStatement);
        }
        LOG.info("Collected mini statement for the accountId {} " +
                        "and the transaction size is : {}",
                accountId,miniStatements.size());
        return miniStatements;
    }

    @Override
    public AccountEntity deleteAccount(AccountRequestDto account) throws CustomException {
        Optional<AccountEntity> accountEntity = accountRepository.findById(account.getAccountId());
        if(!accountEntity.isPresent()){
            LOG.info("Not able to delete account Id : {}", account.getAccountId());
            throw new CustomException(CustomErrors.NO_ACCOUNT_AVAILABLE);
        }
        accountEntity.get().setAccountStatus(AccountStatus.INACTIVE);
        accountRepository.save(accountEntity.get());
        LOG.info("Deleted account Id : {}", account.getAccountId());
        return accountEntity.get();
    }
}
