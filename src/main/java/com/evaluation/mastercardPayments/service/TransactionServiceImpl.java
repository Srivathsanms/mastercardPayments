package com.evaluation.mastercardPayments.service;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionDetailsRepository;

    @Override
    @Transactional
    public void addAmount(AddAmountDto addAmountDto) throws CustomException {
        Optional<AccountEntity> accountInfo = accountRepository.findById(addAmountDto.getAccountId());
        if(!accountInfo.isPresent()){
            throw new CustomException(CustomErrors.INVALID_ACCOUNT_NUMBER);
        }
        accountInfo.get().setBalance(addAmountDto.getAmount().add(accountInfo.get().getBalance()));
        accountRepository.save(accountInfo.get());

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

    private AccountEntity validateReceiverAccount(TransferRequestDto transferreques) throws CustomException {
        Optional<AccountEntity> account = accountRepository.findById(transferreques.getReceiverId());
        if (!account.isPresent()) {
            throw new CustomException(CustomErrors.INVALID_CREDITOR_ACCOUNT);
        }
        AccountEntity receiverAccount = account.get();
        if (receiverAccount.getAccountStatus() == AccountStatus.INACTIVE) {
            throw new CustomException(CustomErrors.INACTIVE_CREDITOR_ACCOUNT);
        }
        return receiverAccount;
    }


    /*Make sure Sender Account must exists and ACTIVE and balance amount must be more than transfer amount*/
    private AccountEntity validateSenderAccount(TransferRequestDto transferRequest) throws CustomException {
        Optional<AccountEntity> account = accountRepository.findById(transferRequest.getSenderId());
        if (!account.isPresent()) {
            throw new CustomException(CustomErrors.INVALID_DEBTOR_ACCOUNT);
        }
        AccountEntity senderAccount = account.get();
        if (senderAccount.getAccountStatus() == AccountStatus.INACTIVE) {
            throw new CustomException(CustomErrors.INACTIVE_DEBTOR_ACCOUNT);
        }

        if (!(senderAccount.getBalance().compareTo(transferRequest.getAmount()) >= 0)) {
            throw new CustomException(CustomErrors.INSUFFICIENT_FUNDS_AVAILABLE);
        }
        return senderAccount;
    }



}