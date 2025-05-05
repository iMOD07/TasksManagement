package com.TaskManagement.SpringBoot;

import com.TaskManagement.SpringBoot.model.AdminUser;
import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

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
            admin.setEmail("admin@sys.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            adminRepo.save(admin);
            System.out.println("✅ Default system admin created: admin@sys.com / admin123");
        }
    }
}
