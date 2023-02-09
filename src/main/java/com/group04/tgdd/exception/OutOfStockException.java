package com.group04.tgdd.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OutOfStockException extends RuntimeException {
    private String message;
}
