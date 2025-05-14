package com.TaskManagement.SpringBoot.controller;

import com.TaskManagement.SpringBoot.exception.ResourceLockedException;
import com.TaskManagement.SpringBoot.exception.ResourceNotFoundException;
import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.model.UserEmployee;
import com.TaskManagement.SpringBoot.service.User.UserServiceEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private UserServiceEmployee employeeService;


    // Get All Employees - only ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/")
    public ResponseEntity<List<UserEmployee>> getAllEmployees() {
        List<UserEmployee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // Get Employee by ID - only Admin
    @PreAuthorize("hasRole('ADMIN','SUPERVISOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UserEmployee> getEmployeeById(@PathVariable Long id) {
        Optional<UserEmployee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Delete Account by ADMIN and by EMPLOYEE Yourself
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteUserEmployee(@PathVariable Long employeeId,
                                                     Authentication authentication) {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserEmployee userEmployee) {
            boolean isAdminEmployee = userEmployee.getRole() == Role.ADMIN;

            // Only regular EMPLOYEE can't delete others
            if (!isAdminEmployee && !userEmployee.getId().equals(employeeId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You do not have permission to delete another Employee.");
            }
        }

        // AdminUser or AdminEmployee is allowed to delete
        try {
            employeeService.deleteEmployee(employeeId);
            return ResponseEntity.ok("The Employee has been successfully deleted.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ResourceLockedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("You cannot delete the account because you have Tasks.");
        }
    }


    // Update Role to SUPERVISOR
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/updateRole/{employeeId}")
    public ResponseEntity<?> updateEmployeeRole(@PathVariable Long employeeId,
                                                @RequestParam String role,
                                                Authentication authentication) {

        Object principal = authentication.getPrincipal();

        Role newRole;
        try {
            newRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role: " + role);
        }

        // Only allow changing role to SUPERVISOR
        if (newRole != Role.SUPERVISOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only SUPERVISOR role assignment is allowed.");
        }

        if (principal instanceof UserEmployee currentUser) {
            boolean isAdminEmp = currentUser.getRole() == Role.ADMIN;

            // Prevent changing the role for himself
            if (isAdminEmp && currentUser.getId().equals(employeeId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not allowed to change your own role.");
            }
        }

        UserEmployee updatedEmployee = employeeService.updateEmployeeRole(employeeId, newRole);
        return ResponseEntity.ok(updatedEmployee);
    }

}