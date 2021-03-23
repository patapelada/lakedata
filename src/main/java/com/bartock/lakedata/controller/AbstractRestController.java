package com.bartock.lakedata.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bartock.lakedata.security.UnauthorizedException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRestController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    protected Map<String, List<FieldError>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<FieldError>> result = new HashMap<>();
        result.put("fieldValidationErrors", ex.getBindingResult().getFieldErrors());
        log.error("Returning 400, caused by", ex);

        return result;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ IllegalArgumentException.class, NullPointerException.class })
    protected Map<String, String> handleAssertExceptions(Exception ex) {
        Map<String, String> result = new HashMap<>();
        result.put("errors", ex.getMessage());

        log.error("Returning 400, caused by", ex);
        return result;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ UnauthorizedException.class })
    protected Map<String, String> handleUnauthorizedExceptions(Exception ex) {
        Map<String, String> result = new HashMap<>();
        result.put("error", ex.getMessage());
        log.error("Returning 401, caused by", ex);

        return result;
    }

}
