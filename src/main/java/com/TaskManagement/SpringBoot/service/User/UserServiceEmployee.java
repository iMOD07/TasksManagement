package com.TaskManagement.SpringBoot.service.User;

import com.TaskManagement.SpringBoot.exception.ResourceLockedException;
import com.TaskManagement.SpringBoot.exception.ResourceNotFoundException;
import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.TaskManagement.SpringBoot.model.UserEmployee;
import com.TaskManagement.SpringBoot.repository.Users.UserEmployeeRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.management.RuntimeErrorException;
import java.util.Optional;
import java.util.List;

@Service
public class UserServiceEmployee {

    @Autowired
    private UserEmployeeRepository employeeRepository;

    @Autowired
    private TaskRepository taskRepository;


    public UserEmployee registerEmployee(String fullName,
                                         String email,
                                         String passwordHash,
                                         String mobileNumber,
                                         String department,
                                         String jobTitle) {

        if (employeeRepository.findByEmail(email).isPresent()) {
            throw new ResourceNotFoundException("This email is already registered.");
        }

        if (employeeRepository.findByMobileNumber(mobileNumber).isPresent()) {
            throw new ResourceNotFoundException("This Mobile Number is already registered");
        }

        if (mobileNumber.length() != 10 ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must enter only 10 numbers.");
        }


        UserEmployee employee = new UserEmployee();
        employee.setFullName(fullName);
        employee.setEmail(email);
        employee.setPasswordHash(passwordHash);
        employee.setMobileNumber(mobileNumber);
        employee.setDepartment(department);
        employee.setJobTitle(jobTitle);
        employee.setRole(Role.EMPLOYEE);
        return employeeRepository.save(employee);
    }

    public Optional<UserEmployee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public boolean existsById(Long employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    // Employee search function by email
    public Optional<UserEmployee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    // Get all employees
    public List<UserEmployee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Optional<UserEmployee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }


    // Delete Employee
    @Transactional
    public void deleteEmployee(Long employeeId) {
        UserEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (taskRepository.countByEmployeeId(employeeId) > 0) {
            throw new ResourceLockedException("Cannot delete Client because there are Tasks.");
        }
        employeeRepository.delete(employee);
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
