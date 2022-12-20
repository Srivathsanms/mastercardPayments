package com.evaluation.mastercardPayments.exception;


public class CustomErrorResponse {

    private CustomErrors errorCode;
    private String errorMessage;

    public CustomErrorResponse() {
    }

    public CustomErrorResponse(CustomErrors errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CustomErrors getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(CustomErrors errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}


