package com.springboot.careassist.exception;

public class InvalidClaimStatusException extends RuntimeException {
    public InvalidClaimStatusException(String message) {
        super(message);
    }
}
