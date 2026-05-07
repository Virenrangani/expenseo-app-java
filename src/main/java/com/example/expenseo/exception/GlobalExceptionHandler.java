package com.example.expenseo.exception;

import com.example.expenseo.dto.ApiResponseError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError> globalValidationErrors(
            MethodArgumentNotValidException ex , HttpServletRequest request
    ){
        Map<String , String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(),error.getDefaultMessage())
        );

        ApiResponseError response = ApiResponseError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed for one or more fields")
                .path(request.getRequestURI())
                .validationsErrors(errors)
                .build();

        log.warn("Validation error on path {}: {}", request.getRequestURI(), errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseError> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {

        log.warn("Runtime exception on path {}: {}", request.getRequestURI(), ex.getMessage());

        ApiResponseError response = ApiResponseError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseError> handleAllUncaughtException(
            Exception ex, HttpServletRequest request) {

        log.error("Unknown error occurred on path {}", request.getRequestURI(), ex);

        ApiResponseError response = ApiResponseError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred. Please try again later.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
