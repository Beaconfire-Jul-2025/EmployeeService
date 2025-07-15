package org.beaconfire.exception;

import org.beaconfire.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name:beaconfire}")
    private String appName;

    private String traceId() {
        return MDC.get("traceId");
    }

    private String host() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private ApiResponse<?> fail(String errorCode, String errorMessage, int showType) {
        return ApiResponse.builder()
                .success(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .showType(showType)
                .traceId(traceId())
                .host(host())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return fail("400001", msg, 2);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse<?> notFound(NoSuchElementException ex) {
        return fail("404001", ex.getMessage(), 2);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> badRequest(IllegalArgumentException ex) {
        return fail("400002", ex.getMessage(), 2);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> other(Exception ex) {
        return fail("500000", "The system is busy, please try again later", 2);
    }

    // TODO: Add specific exception handlers for your application, wrap them in ApiResponse
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(Collections.singletonMap("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<?> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return new ResponseEntity<>(
                Collections.singletonMap("message", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<?> handleEmployeeAlreadyExists(EmployeeAlreadyExistsException ex) {
        return new ResponseEntity<>(Collections.singletonMap("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDocumentNotFoundException(DocumentNotFoundException ex) {
        return ResponseEntity.status(404).body(Collections.singletonMap("message", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        logger.error("Unhandled exception occurred", e);
        return new ResponseEntity<>(Collections.singletonMap("message", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}