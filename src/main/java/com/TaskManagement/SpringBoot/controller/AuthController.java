package com.TaskManagement.SpringBoot.controller;

import com.TaskManagement.SpringBoot.dto.AuthResponse;
import com.TaskManagement.SpringBoot.dto.ClientRegisterRequest;
import com.TaskManagement.SpringBoot.dto.EmployeeRegisterRequest;
import com.TaskManagement.SpringBoot.dto.LoginRequest;
import com.TaskManagement.SpringBoot.model.*;
import com.TaskManagement.SpringBoot.repository.Users.AdminUserRepository;
import com.TaskManagement.SpringBoot.security.JwtUtil;
import com.TaskManagement.SpringBoot.service.User.UserServiceClient;
import com.TaskManagement.SpringBoot.service.User.UserServiceEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserServiceEmployee employeeService;

    @Autowired
    private UserServiceClient clientService;

    @Autowired
    private AdminUserRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register Employee
    @PostMapping("/register/employee")
    public ResponseEntity<String> registerEmployee(@RequestBody EmployeeRegisterRequest request) {
        UserEmployee employee = employeeService.registerEmployee(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getMobileNumber(),
                request.getDepartment(),
                request.getJobTitle()
        );
        return ResponseEntity.ok("✅ Employee registered successfully.");
    }

    // Register Client
    @PostMapping("/register/client")
    public ResponseEntity<String> registerClient(@RequestBody ClientRegisterRequest request) {
        UserClient client = clientService.registerClient(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getMobileNumber(),
                request.getCompanyName(),
                request.getAddress()
        );
        return ResponseEntity.ok("✅ Client registered successfully.");
    }

    // Login Admin
    @PostMapping("/login/admin")
    public ResponseEntity<AuthResponse> loginAdmin(@RequestBody LoginRequest request) {
        Optional<AdminUser> adminOptional = adminRepo.findByEmail(request.getEmail());

        if (adminOptional.isEmpty() ||
                !passwordEncoder.matches(request.getPassword(), adminOptional.get().getPasswordHash())) {
            return ResponseEntity.status(401).body(new AuthResponse(null, null));
        }

        AdminUser admin = adminOptional.get();
        String token = jwtUtil.generateToken(admin.getEmail(), admin.getRole().name());

        return ResponseEntity.ok(new AuthResponse(token, admin.getRole().name()));
    }

    // Login Employee
    @PostMapping("/login/employee")
    public ResponseEntity<AuthResponse> loginEmployee(@RequestBody LoginRequest request) {
        Optional<UserEmployee> employeeOptional = employeeService.findByEmail(request.getEmail());

        if (employeeOptional.isEmpty() ||
                !passwordEncoder.matches(request.getPassword(), employeeOptional.get().getPasswordHash())) {
            return ResponseEntity.status(401).body(new AuthResponse(null, null));
        }

        UserEmployee employee = employeeOptional.get();
        String token = jwtUtil.generateToken(employee.getEmail(), employee.getRole().name());

        return ResponseEntity.ok(new AuthResponse(token, employee.getRole().name()));
    }

    // Login Client
    @PostMapping("/login/client")
    public ResponseEntity<AuthResponse> loginClient(@RequestBody LoginRequest request) {
        Optional<UserClient> clientOptional = clientService.findByEmail(request.getEmail());

        if (clientOptional.isEmpty() ||
                !passwordEncoder.matches(request.getPassword(), clientOptional.get().getPasswordHash())) {
            return ResponseEntity.status(401).body(new AuthResponse(null, null));
        }

        UserClient client = clientOptional.get();
        String token = jwtUtil.generateToken(client.getEmail(), client.getRole().name());

        return ResponseEntity.ok(new AuthResponse(token, client.getRole().name()));
    }
}
