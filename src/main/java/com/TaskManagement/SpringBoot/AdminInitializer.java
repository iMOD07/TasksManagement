package com.TaskManagement.SpringBoot;

import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.repository.Users.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


import com.TaskManagement.SpringBoot.model.AdminUser;

import com.TaskManagement.SpringBoot.repository.Users.UserEmployeeRepository;

@Component
public class AdminInitializer {

    @Autowired
    private AdminUserRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (adminRepo.count() == 0) {
            AdminUser admin = new AdminUser();
            admin.setEmail("admin@admin.com");
            admin.setPasswordHash(passwordEncoder.encode("Aa@102030"));
            admin.setFullName("System Admin");
            admin.setMobileNumber("0503369271");
            admin.setRole(Role.ADMIN);
            adminRepo.save(admin);
            System.out.println("✅ Default system admin created: admin@admin.com / Aa@102030");
        }
    }
}
