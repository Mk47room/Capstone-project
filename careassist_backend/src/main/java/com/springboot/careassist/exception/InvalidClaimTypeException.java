package com.springboot.careassist.exception;

public class InvalidClaimTypeException extends RuntimeException {
    public InvalidClaimTypeException(String message) {
        super(message);
    }
}
