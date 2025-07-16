package org.beaconfire.employee.exception;

import org.beaconfire.employee.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.InetAddress;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return fail("400002", ex.getMessage(), 2);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ApiResponse<?> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return fail("404001", ex.getMessage(), 2);
    }

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ApiResponse<?> handleEmployeeAlreadyExists(EmployeeAlreadyExistsException ex) {
        return fail("400003", ex.getMessage(), 2);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ApiResponse<?> handleDocumentNotFoundException(DocumentNotFoundException ex) {
        return fail("404002", ex.getMessage(), 2);
    }

    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ApiResponse<?> handleDocumentAlreadyExists(DocumentAlreadyExistsException ex) {
        return fail("400004", ex.getMessage(), 2);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse<?> handleNoSuchElementException(NoSuchElementException ex) {
        return fail("404003", ex.getMessage(), 2);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        logger.error("Unhandled exception occurred", e);
        return fail("500000", "The system is busy, please try again later", 2);
    }
}
