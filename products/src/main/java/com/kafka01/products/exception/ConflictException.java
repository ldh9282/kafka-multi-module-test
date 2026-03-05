package com.kafka01.products.exception;

/**
 * 충돌 예외
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}