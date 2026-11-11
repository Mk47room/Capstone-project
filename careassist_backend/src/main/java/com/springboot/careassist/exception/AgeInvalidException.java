package com.springboot.careassist.exception;

public class AgeInvalidException extends RuntimeException {
    public AgeInvalidException(String message) {
        super(message);
    }
}
