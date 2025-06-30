package com.andre.account.exception;

public class SavingsAccountManualCreationException extends RuntimeException {
    public SavingsAccountManualCreationException() {
        super("SAVINGS accounts cannot be created manually.");
    }
}
