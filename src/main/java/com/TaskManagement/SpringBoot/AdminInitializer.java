package com.TaskManagement.SpringBoot;

import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.repository.Users.UserAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


import com.TaskManagement.SpringBoot.model.UserAdmin;

@Component
public class AdminInitializer {

    @Autowired
    private UserAdminRepository userAdminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userAdminRepository.count() == 0) {
            UserAdmin admin = new UserAdmin();
            admin.setEmail("admin@admin.com");
            admin.setPasswordHash(passwordEncoder.encode("Aa@102030"));
            admin.setFullName("System Admin");
            admin.setMobileNumber("0503369271");
            admin.setRole(Role.ADMIN);
            userAdminRepository.save(admin);
            System.out.println("✅ Default system admin created: admin@admin.com / Aa@102030");
        }
    }
}
