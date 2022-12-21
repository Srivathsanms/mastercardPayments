package com.evaluation.mastercardPayments.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import com.evaluation.mastercardPayments.model.TransferRequestDto;
import com.evaluation.mastercardPayments.entity.TransactionEntity;
import com.evaluation.mastercardPayments.model.TransactionType;
import com.evaluation.mastercardPayments.repository.AccountRepository;

import com.evaluation.mastercardPayments.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Primary
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionDetailsRepository;


    public void createAccount(AccountRequestDto accountInfoRequest) throws CustomException {
        AccountEntity account = new AccountEntity().builder()
                .id(accountInfoRequest.getAccountId())
                .balance(BigDecimal.ZERO)
                .currencyType(CurrencyType.valueOf(accountInfoRequest.getCurrency()))
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        Optional<AccountEntity> findAccount = accountRepository.findById(account.getId());
        if (findAccount.isPresent()) {
            throw new CustomException(CustomErrors.ACCOUNT_ALREADY_EXIST);
        }
        accountRepository.save(account);
    }



    public AccountEntity getAccountDetails(String accountId) throws CustomException {
        Optional<AccountEntity> accountInfo = accountRepository.findById(accountId);
        if (accountInfo.isPresent()) {
            return accountInfo.get();
        }
        throw new CustomException(CustomErrors.INVALID_ACCOUNT_NUMBER);
    }

    public List<AccountEntity> getAllAccountDetails() throws CustomException {
        List<AccountEntity> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new CustomException("No Account present");
        }
        return accounts;
    }

    @Transactional
    public TransactionEntity transferMoney(TransferRequestDto paymentTransferRequest) throws CustomException {
        AccountEntity debtorAccount = validateSenderAccount(paymentTransferRequest);
        AccountEntity creditorAccount = validateReceiverAccount(paymentTransferRequest);

        //Debit from Sender Account
        debtorAccount.setBalance(debtorAccount.getBalance().subtract(paymentTransferRequest.getAmount()));
        //accountRepository.save(debtorAccount);

        //Credit Receiver Account
        creditorAccount.setBalance(creditorAccount.getBalance().add(paymentTransferRequest.getAmount()));
        //accountRepository.save(creditorAccount);


        TransactionEntity transactionDetails = new TransactionEntity().builder()
                .senderId(debtorAccount.getId())
                .receiverId(creditorAccount.getId())
                .txnAmount(paymentTransferRequest.getAmount())
                .localDateTime(LocalDateTime.now())
                .currencyType(CurrencyType.valueOf(paymentTransferRequest.getCurrency()))
                .build();

        //Log in transaction table
        transactionDetailsRepository.save(transactionDetails);
        return transactionDetails;
    }

    public List<MiniStatement> getMiniStatement(String accountId) throws CustomException {
        getAccountDetails(accountId);
        List<TransactionEntity> transactions = transactionDetailsRepository.findTransactionsForAccount(Integer.parseInt(accountId));
        List<MiniStatement> miniStatements = new ArrayList<>();
        if(transactions.isEmpty()) {
            return Collections.emptyList();
        }
        for (TransactionEntity txn : transactions) {
            MiniStatement miniStatement = new MiniStatement().builder()
                    .accountId(txn.getSenderId().equals(accountId) ? txn.getReceiverId() : txn.getSenderId())
                    .transactionAmount(txn.getTxnAmount())
                    .currency(txn.getCurrencyType())
                    .transactionType(txn.getSenderId().equals(accountId) ? TransactionType.DEBIT : TransactionType.CREDIT)
                    .transactionTime(txn.getLocalDateTime())
                    .build();
            miniStatements.add(miniStatement);
        }
        return miniStatements;
    }

    /*Make sure Sender Account must exists and ACTIVE and balance amount must be more than transfer amount*/
    private AccountEntity validateSenderAccount(TransferRequestDto transferRequest) throws CustomException {
        Optional<AccountEntity> account = accountRepository.findById(transferRequest.getSenderId());
        if (!account.isPresent()) {
            throw new CustomException("Invalid Sender Account : " + transferRequest.getSenderId());
        }
        AccountEntity senderAccount = account.get();
        if (senderAccount.getAccountStatus() == AccountStatus.INACTIVE) {
            throw new CustomException("Sender Account : " + transferRequest.getSenderId() + " not Active");
        }

        if (!(senderAccount.getBalance().compareTo(transferRequest.getAmount()) >= 0)) {
            throw new CustomException("Insufficient funds available");
        }
        return senderAccount;
    }

    /*Sender Account must be valid and ACTIVE */
    private AccountEntity validateReceiverAccount(TransferRequestDto transferreques) throws CustomException {
        Optional<AccountEntity> account = accountRepository.findById(transferreques.getReceiverId());
        if (!account.isPresent()) {
            throw new CustomException("Invalid Receiver Account : " + transferreques.getReceiverId());
        }
        AccountEntity receiverAccount = account.get();
        if (receiverAccount.getAccountStatus() == AccountStatus.INACTIVE) {
            throw new CustomException("Receiver Account : " + transferreques.getReceiverId() + " not Active");
        }
        return receiverAccount;
    }


}
