package com.TaskManagement.SpringBoot.exception;

public class ResourceLockedException extends RuntimeException {
    public ResourceLockedException(String message) {
        super(message);
    }
}
