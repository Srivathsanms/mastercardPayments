package com.evaluation.mastercardPayments.service;

import com.evaluation.mastercardPayments.controller.AccountController;
import com.evaluation.mastercardPayments.dto.AddAmountDto;
import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.entity.TransactionEntity;
import com.evaluation.mastercardPayments.exception.CustomErrors;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.model.AccountStatus;
import com.evaluation.mastercardPayments.model.CurrencyType;
import com.evaluation.mastercardPayments.model.TransferRequestDto;
import com.evaluation.mastercardPayments.repository.AccountRepository;

import com.evaluation.mastercardPayments.repository.TransactionRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class TransactionServiceImpl implements TransactionService {


    private static final Logger LOG = LogManager.getLogger(AccountController.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionDetailsRepository;


    @Override
    @Transactional
    public void addAmount(AddAmountDto addAmountDto) throws CustomException {
        Optional<AccountEntity> accountEntity = accountRepository.findById(addAmountDto.getAccountId());
        if(!accountEntity.isPresent()){
            LOG.info("Account  {} not available to add amount ", addAmountDto.getAccountId());
            throw new CustomException(CustomErrors.INVALID_ACCOUNT_NUMBER);
        }
        accountEntity.get().setBalance(addAmountDto.getAmount().add(accountEntity.get().getBalance()));
        accountRepository.save(accountEntity.get());

    }

    @Transactional
    public TransactionEntity transferAmount(TransferRequestDto paymentTransferRequest) throws CustomException {
        LOG.info("Transfer amount initiated ");
        AccountEntity debtorAccount = validateSenderAccount(paymentTransferRequest);
        AccountEntity creditorAccount = validateReceiverAccount(paymentTransferRequest);
        debtorAccount.setBalance(debtorAccount.getBalance().subtract(paymentTransferRequest.getAmount()));
        creditorAccount.setBalance(creditorAccount.getBalance().add(paymentTransferRequest.getAmount()));
        TransactionEntity transactionDetails = new TransactionEntity().builder()
                .debtorAccount(debtorAccount.getId())
                .creditorAccount(creditorAccount.getId())
                .txAmount(paymentTransferRequest.getAmount())
                .localDateTime(LocalDateTime.now())
                .currencyType(CurrencyType.valueOf(paymentTransferRequest.getCurrency()))
                .build();

        transactionDetailsRepository.save(transactionDetails);
        return transactionDetails;
    }

    private AccountEntity validateReceiverAccount(TransferRequestDto transferRequest) throws CustomException {
        Optional<AccountEntity> account = accountRepository.findById(transferRequest.getCreditorAccount());
        if (!account.isPresent()) {
            LOG.info("Invalid creditor account {}", transferRequest.getDebtorAccount());
            throw new CustomException(CustomErrors.INVALID_CREDITOR_ACCOUNT);
        }
        AccountEntity receiverAccount = account.get();
        if (receiverAccount.getAccountStatus() == AccountStatus.INACTIVE) {
            LOG.info("Inactive creditor account {} ", transferRequest.getDebtorAccount());
            throw new CustomException(CustomErrors.INACTIVE_CREDITOR_ACCOUNT);
        }
        return receiverAccount;
    }


    private AccountEntity validateSenderAccount(TransferRequestDto transferRequest) throws CustomException {
        Optional<AccountEntity> account = accountRepository.findById(transferRequest.getDebtorAccount());
        if (!account.isPresent()) {
            LOG.info("Invalid debtor account {}", transferRequest.getDebtorAccount());
            throw new CustomException(CustomErrors.INVALID_DEBTOR_ACCOUNT);
        }
        AccountEntity senderAccount = account.get();
        if (senderAccount.getAccountStatus() == AccountStatus.INACTIVE) {
            LOG.info("Debtor account is Invalid {}", transferRequest.getDebtorAccount());
            throw new CustomException(CustomErrors.INACTIVE_DEBTOR_ACCOUNT);
        }

        if (!(senderAccount.getBalance().compareTo(transferRequest.getAmount()) >= 0)) {
            LOG.info("Insuficient fund for debtor {} ", transferRequest.getDebtorAccount());
            throw new CustomException(CustomErrors.INSUFFICIENT_FUNDS_AVAILABLE);
        }
        return senderAccount;
    }



}
