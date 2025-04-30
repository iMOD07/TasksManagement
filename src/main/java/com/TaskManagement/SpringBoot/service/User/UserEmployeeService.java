package com.TaskManagement.SpringBoot.service.User;

import com.TaskManagement.SpringBoot.exception.ResourceLockedException;
import com.TaskManagement.SpringBoot.exception.ResourceNotFoundException;
import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.model.UserEmployee;
import com.TaskManagement.SpringBoot.repository.TaskRepository;
import com.TaskManagement.SpringBoot.repository.UserEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class UserEmployeeService {

    @Autowired
    private UserEmployeeRepository employeeRepository;

    @Autowired
    private TaskRepository taskRepository;

    // Get all employees
    public List<UserEmployee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Optional<UserEmployee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public UserEmployee registerEmployee(String fullName,
                                         String email,
                                         String passwordHash,
                                         String mobileNumber,
                                         String department,
                                         String jobTitle) {
        if (employeeRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (employeeRepository.findByMobileNumber(mobileNumber).isPresent()) {
            throw new RuntimeException("Mobile Number already exists");
        }

        UserEmployee employee = new UserEmployee();
        employee.setFullName(fullName);
        employee.setEmail(email);
        employee.setPasswordHash(passwordHash);
        employee.setMobileNumber(mobileNumber);
        employee.setDepartment(department);
        employee.setJobTitle(jobTitle);
        employee.setRole(Role.EMPLOYEE); //
        return employeeRepository.save(employee);
    }

    // Employee search function by email
    public Optional<UserEmployee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public boolean existsById(Long employeeId) {
        return employeeRepository.existsById(employeeId);
    }


    // Delete Employee
    public void deleteEmployee(Long employeeId) {
        if (taskRepository.existsByAssignedToId(employeeId)){
            throw new ResourceLockedException("Cannot delete employee because there are assigned tasks.");
        }

        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found");
        }

        employeeRepository.deleteById(employeeId);
    }



    public UserEmployee updateEmployeeRole(Long employeeId, Role newRole) {
        Optional<UserEmployee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) {
            throw new RuntimeException("Employee not found");
        }
        UserEmployee employee = optionalEmployee.get();
        employee.setRole(newRole);
        return employeeRepository.save(employee);
    }
}
