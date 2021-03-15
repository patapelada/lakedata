package com.bartock.lakedata.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class AbstractRestController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    protected Map<String, List<FieldError>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<FieldError>> result = new HashMap<>();
        result.put("fieldValidationErrors", ex.getBindingResult().getFieldErrors());

        return result;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ IllegalArgumentException.class, NullPointerException.class })
    protected Map<String, String> handleAssertExceptions(Exception ex) {
        Map<String, String> result = new HashMap<>();
        result.put("errors", ex.getMessage());

        return result;
    }

}
