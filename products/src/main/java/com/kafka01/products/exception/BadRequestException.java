package com.kafka01.products.exception;

/**
 * 잘못된 요청 예외
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}