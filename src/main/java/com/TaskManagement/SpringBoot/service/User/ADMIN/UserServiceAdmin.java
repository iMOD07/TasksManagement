package com.TaskManagement.SpringBoot.service.User.ADMIN;

import com.TaskManagement.SpringBoot.model.AdminUser;
import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.repository.Users.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceAdmin {

    @Autowired
    private AdminUserRepository adminRepo;

    public AdminUser registerAdmin(String fullName,
                                   String email,
                                   String passwordHash,
                                   String mobileNumber) {
        AdminUser admin = new AdminUser();
        admin.setFullName(fullName);
        admin.setEmail(email);
        admin.setPasswordHash(passwordHash);
        admin.setMobileNumber(mobileNumber);
        admin.setRole(Role.ADMIN);
        return adminRepo.save(admin);
    }

    public Optional<AdminUser> findByEmail(String email) {
        return adminRepo.findByEmail(email);
    }

    public Optional<AdminUser> findById(Long id) {
        return adminRepo.findById(id);
    }
}
