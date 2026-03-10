package com.kafka01.products.exception;

/**
 * 404 리소스 없음
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String message, Throwable e) {
        super(message);
        initCause(e);
    }
    public NotFoundException(Throwable e) {
        super();
        initCause(e);
    }
}