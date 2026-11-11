package com.springboot.careassist.config;

import com.springboot.careassist.dto.response.ErrorDto;
import com.springboot.careassist.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("Id not found in DB");
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto(
                        e.getMessage(),
                        "Id not found in DB",
                        Instant.now()
                )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ){
        Map<String,String> map = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> list = bindingResult.getFieldErrors();
        list.forEach(fieldError -> map.put(fieldError.getField(),fieldError.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(map);
    }

    @ExceptionHandler(AgeInvalidException.class)
    public ResponseEntity<ErrorDto> handleAgeInvalidException(AgeInvalidException e){
        log.warn("Age constraint violation: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto(
                                e.getMessage(),
                                "Plan has a age constrain",
                                Instant.now()
                        )
                );
    }

    @ExceptionHandler(InvalidCallException.class)
    public ResponseEntity<ErrorDto> handleInvalidCallException(InvalidCallException e){
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto(
                        e.getMessage(),
                        "Cannot perform Empty Update",
                        Instant.now()
                ));
    }

    @ExceptionHandler(PolicyExpireException.class)
    public ResponseEntity<ErrorDto> handlePolicyExpireException(PolicyExpireException e){
        log.warn("Policy expiration error: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto(
                        e.getMessage(),
                        "Policy Duration Mismatching",
                        Instant.now()
                ));
    }
    @ExceptionHandler(OwnershipException.class)
    public ResponseEntity<ErrorDto> handleOwnershipException(OwnershipException e){
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto(
                        e.getMessage(),
                        "Operation not allowed!",
                        Instant.now()
                ));
    }

    @ExceptionHandler(InvalidClaimStatusException.class)
    public ResponseEntity<ErrorDto> handleInvalidClaimStatusException(InvalidClaimStatusException e){
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto(
                        e.getMessage(),
                        "Claim status invalid",
                        Instant.now()
                ));
    }

    @ExceptionHandler(InvalidClaimTypeException.class)
    public ResponseEntity<ErrorDto> handleInvalidClaimTypeException(InvalidClaimTypeException e){
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto(
                        e.getMessage(),
                        "Claim type invalid",
                        Instant.now()
                ));
    }

    @ExceptionHandler(InactiveUserException.class)
    public ResponseEntity<ErrorDto> handleInactiveUserException(InactiveUserException e){
        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .body(new ErrorDto(
                        e.getMessage(),
                        "Contact ADMIN admin@careassist.com",
                        Instant.now()
                ));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException(
            NullPointerException e
    ){
        //always reply the object not the string

        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .body(
                        new ErrorDto(e.getMessage(),
                                "File is mandatory",
                                Instant.now())
                );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDto> handleIOException(
            IOException e
    ){
        //always reply the object not the string

        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .body(
                        new ErrorDto(e.getMessage(),
                                "File is mandatory",
                                Instant.now())
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(
            Exception e
    ){
        return ResponseEntity
                .badRequest()
                .body(
                        new ErrorDto(e.getMessage(),
                                "Invalid data passed",
                                Instant.now())
                );
    }

}
