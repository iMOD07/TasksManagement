package com.TaskManagement.SpringBoot.controller;

import com.TaskManagement.SpringBoot.dto.AuthResponse;
import com.TaskManagement.SpringBoot.dto.ClientRegisterRequest;
import com.TaskManagement.SpringBoot.dto.EmployeeRegisterRequest;
import com.TaskManagement.SpringBoot.dto.LoginRequest;
import com.TaskManagement.SpringBoot.model.*;
import com.TaskManagement.SpringBoot.repository.Users.UserAdminRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserClientRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserEmployeeRepository;
import com.TaskManagement.SpringBoot.security.JwtUtil;
import com.TaskManagement.SpringBoot.service.User.UserServiceClient;
import com.TaskManagement.SpringBoot.service.User.UserServiceEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserClientRepository clientRepo;
    private final UserAdminRepository adminRepo;
    private final UserEmployeeRepository employeeRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserServiceClient clientService;
    private final UserServiceEmployee employeeService;

    // ✅ تسجيل دخول العميل فقط
    @PostMapping("/login/client")
    public ResponseEntity<?> loginClient(@RequestBody LoginRequest request) {
        var client = clientRepo.findByEmail(request.getEmail());
        if (client.isPresent() && passwordEncoder.matches(request.getPassword(), client.get().getPasswordHash())) {
            String token = jwtUtil.generateToken(buildUserDetails(client.get(), "ROLE_CLIENT"));
            return ResponseEntity.ok(new AuthResponse(token, "CLIENT"));
        }
        return ResponseEntity.status(401).body("❌ Invalid client credentials");
    }

    // ✅ تسجيل دخول الموظف فقط
    @PostMapping("/login/employee")
    public ResponseEntity<?> loginEmployee(@RequestBody LoginRequest request) {
        var employee = employeeRepo.findByEmail(request.getEmail());
        if (employee.isPresent() && passwordEncoder.matches(request.getPassword(), employee.get().getPasswordHash())) {
            String token = jwtUtil.generateToken(buildUserDetails(employee.get(), "ROLE_EMPLOYEE"));
            return ResponseEntity.ok(new AuthResponse(token, "EMPLOYEE"));
        }
        return ResponseEntity.status(401).body("❌ Invalid employee credentials");
    }

    // ✅ تسجيل دخول الأدمن فقط
    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request) {
        var admin = adminRepo.findByEmail(request.getEmail());
        if (admin.isPresent() && passwordEncoder.matches(request.getPassword(), admin.get().getPasswordHash())) {
            String token = jwtUtil.generateToken(buildUserDetails(admin.get(), "ROLE_ADMIN"));
            return ResponseEntity.ok(new AuthResponse(token, "ADMIN"));
        }
        return ResponseEntity.status(401).body("❌ Invalid admin credentials");
    }

    // ✅ تسجيل عميل
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

    // ✅ تسجيل موظف
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

    // ✅ تسجيل أدمن (يدوي أو من لوحة تحكم)
    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody LoginRequest request) {
        if (adminRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Admin already exists");
        }

        UserAdmin admin = new UserAdmin();
        admin.setEmail(request.getEmail());
        admin.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        admin.setFullName("Admin");
        admin.setMobileNumber("0559999999");
        admin.setRole(Role.ADMIN);

        adminRepo.save(admin);
        return ResponseEntity.ok("✅ Admin registered successfully.");
    }

    // ✅ بناء UserDetails
    private org.springframework.security.core.userdetails.User buildUserDetails(UserBase user, String role) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
        );
    }
}

