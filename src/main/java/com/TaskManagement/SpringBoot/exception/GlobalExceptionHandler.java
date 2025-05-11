package com.TaskManagement.SpringBoot.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceLockedException.class)
    public ResponseEntity<String> handleResourceLocked(ResourceLockedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Error message for saving duplicate email or mobile phone
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDup(DataIntegrityViolationException ex) {
        String messageError = ex.getMostSpecificCause().getMessage();
        if (messageError != null) {
            if (messageError.contains("mobile_number")) {
                return ResponseEntity.badRequest().body(Map.of("error", "This mobile number is already registered."));
            }
            if (messageError.contains("email")) {
                return ResponseEntity.badRequest().body(Map.of("error", "This email is already registered."));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Attempting to save a duplicate value in a UNIQUE field in the database."));
    }



    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

}
