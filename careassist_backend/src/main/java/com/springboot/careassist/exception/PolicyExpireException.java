package com.springboot.careassist.exception;

public class PolicyExpireException extends RuntimeException {
    public PolicyExpireException(String message) {
        super(message);
    }
}
