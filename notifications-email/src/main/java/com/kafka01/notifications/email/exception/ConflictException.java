package com.kafka01.notifications.email.exception;

/**
 * 충돌 예외
 */
public class ConflictException extends RuntimeException {
    public ConflictException() {
    }
    public ConflictException(String message) {
        super(message);
    }
    public ConflictException(String message, Throwable e) {
        super(message, e);
    }
    public ConflictException(Throwable e) {
        super(e);
    }

}