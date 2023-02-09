package com.group04.tgdd.exception.controller;

import com.group04.tgdd.dto.ResponseDTO;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.exception.ExceptionResq;
import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.exception.PaymentException;
import org.hibernate.exception.LockTimeoutException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PaymentException.class)
    private ResponseEntity<?> paymentExection(PaymentException e){
        return ResponseEntity.status(e.getCode())
                .body(new ExceptionResq(e.getCode(),e.getMessage()));
    }
    @ExceptionHandler(AppException.class)
    private ResponseEntity<?> handleException(AppException e){
        return ResponseEntity.status(e.getCode())
                .body(new ExceptionResq(e.getCode(),e.getMessage()));
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private ResponseEntity<?> handleExceptionLogin(BadCredentialsException e){
        return ResponseEntity.status(403)
                .body(new ExceptionResq(403,"Email or password incorrect"));
    }
    @ExceptionHandler(AuthorizationServiceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private ResponseEntity<?> handleExceptionAuthor(AuthorizationServiceException e){
        return ResponseEntity.status(403)
                .body(new ExceptionResq(403,e.getMessage()));
    }
    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<?> emptyResult(AuthorizationServiceException e){
        return ResponseEntity.status(404)
                .body(new ExceptionResq(404,e.getMessage()));
    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<?> notFound(NotFoundException e){
        return ResponseEntity.status(404)
                .body(new ExceptionResq(404,e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<?> validException(MethodArgumentNotValidException e){
        Map<String,String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResq(400,errors));
    }

    @ExceptionHandler(LockTimeoutException.class)
    private ResponseEntity<?> timeOutQuery(LockTimeoutException e){
        return ResponseEntity.status(500).body(new ExceptionResq(500,"Payment error"));
    }
}
