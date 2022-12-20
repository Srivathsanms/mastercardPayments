package com.evaluation.mastercardPayments.exception;

import lombok.Getter;

@Getter
public enum CustomErrors {
    INSUFFICIENT_FUNDS_AVAILABLE("Insufficient funds available"),
    INVALID_ACCOUNT_NUMBER("Invalid Account Number"),
    INVALID_DEBTOR_ACCOUNT("Invalid Debtor account"),
    ACCOUNT_ALREADY_EXIST("Account already exists"),
    INVALID_ACCOUNT_DETAILS("Invalid account details"),
    INVALID_CREDITOR_ACCOUNT("Invalid creditor account");

    String errorMessage;

    CustomErrors(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
