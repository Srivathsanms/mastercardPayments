package com.evaluation.mastercardPayments.exception;

import lombok.Getter;

@Getter
public enum CustomErrors {
    INSUFFICIENT_FUNDS_AVAILABLE("Insufficient funds available"),
    INVALID_ACCOUNT_NUMBER("Invalid Account Number"),
    CREDITOR_AND_DEBTOR_ACCOUNT_NUMBER_ARE_SAME("Same Creditor and Debtor Account Number"),
    NO_ACCOUNT_AVAILABLE("No Account Present"),
    NO_TRANSACTIONS_FOUND("No transactions found for this account"),
    INVALID_DEBTOR_ACCOUNT("Invalid Debtor account"),
    INACTIVE_DEBTOR_ACCOUNT("Debtor account not active"),
    ACCOUNT_ALREADY_EXIST("Account already exists"),
    INVALID_ACCOUNT_DETAILS("Invalid account details"),
    INVALID_CREDITOR_ACCOUNT("Invalid creditor account"),
    INACTIVE_CREDITOR_ACCOUNT("Creditor account not active");

    String errorMessage;

    CustomErrors(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
