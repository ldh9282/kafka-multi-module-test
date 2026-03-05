package com.kafka01.products.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.kafka01.common.dto.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public <T> BaseResponse<T> handleNotFound(NotFoundException exception) {
        log.error("Not Found", exception);
        return BaseResponse.error(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public <T> BaseResponse<T> handleConflict(ConflictException exception) {
        log.error("Conflict", exception);
        return BaseResponse.error(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler({BadRequestException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> BaseResponse<T> handleBadRequest(Exception exception) {
        log.error("Bad Request", exception);
        return BaseResponse.error(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> BaseResponse<T> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.error("Bad Request", exception);
        String msg = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        log.error("Validation failed: {}", msg);
        return BaseResponse.error(HttpStatus.BAD_REQUEST, msg);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public <T> BaseResponse<T> handleException(Exception exception) {
        log.error("Internal Server Error", exception);
        return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");
    }

}