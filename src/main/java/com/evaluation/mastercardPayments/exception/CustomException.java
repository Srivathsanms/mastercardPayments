package com.evaluation.mastercardPayments.exception;

public class CustomException extends Exception {

    private String messageCode;

    private CustomErrors customErrors;

    public CustomErrors getCustomErrors() {
        return customErrors;
    }

    public void setCustomErrors(CustomErrors customErrors) {
        this.customErrors = customErrors;
    }


    public CustomException() {
        super();
    }
    public CustomException(String messageCode) {
        this.messageCode = messageCode;
    }

    public CustomException(CustomErrors customErrors) {
        this.customErrors = customErrors;
        this.messageCode = customErrors.getErrorMessage();
    }

    public CustomException(String message, String messageCode) {
        super(message);
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }
}
