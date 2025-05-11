package com.TaskManagement.SpringBoot.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Error message for saving duplicate in Database
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String messageError = ex.getMostSpecificCause().getMessage();

        if (messageError == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "An error occurred while saving data."));
        }

        messageError = messageError.toLowerCase();

        if (messageError.contains("duplicate key") || messageError.contains("unique constraint")) {
            return ResponseEntity.badRequest().body(Map.of("error", "The value you entered must be unique."));
        }

        if (messageError.contains("null value") || messageError.contains("not-null constraint")) {
            return ResponseEntity.badRequest().body(Map.of("error", "A required field is missing. Please fill all mandatory fields."));
        }

        if (messageError.contains("violates foreign key constraint")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid reference. The related record does not exist."));
        }
        // fallback
        return ResponseEntity.badRequest().body(Map.of("error", "A data integrity error occurred."));
    }

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

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

}
