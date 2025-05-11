package com.TaskManagement.SpringBoot;

import com.TaskManagement.SpringBoot.model.UserClient;
import com.TaskManagement.SpringBoot.model.UserEmployee;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static UserClient getCurrentClient(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserClient client) {
            return client;
        }
        throw new AccessDeniedException("🚫 Only CLIENT user can access this endpoint.");
    }

    public static UserEmployee getCurrentEmployee(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserEmployee employee) {
            return employee;
        }
        throw new AccessDeniedException("🚫 Only EMPLOYEE user can access this endpoint.");
    }

    public static String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

}
