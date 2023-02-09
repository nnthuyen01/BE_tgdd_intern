package com.group04.tgdd.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class PaymentException extends RuntimeException{
    private int code;
    private String message;
}
