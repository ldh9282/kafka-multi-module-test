package com.kafka01.common.exception;

/**
 * 공통 예외
 */
public class BaseException extends RuntimeException {
    public BaseException() {
    }
    public BaseException(String message) {
        super(message);
    }
    public BaseException(String message, Throwable e) {
        super(message, e);
    }
    public BaseException(Throwable e) {
        super(e);
    }
}