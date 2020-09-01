package com.makif.todoapi.exceptions;

public class DependentItemException extends Exception{

    private int errorCode;
    private String errorMessage;

    public DependentItemException() {
    }

    public DependentItemException(String message) {
        super(message);
    }

    public DependentItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public DependentItemException(Throwable cause) {
        super(cause);
    }

    public DependentItemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
