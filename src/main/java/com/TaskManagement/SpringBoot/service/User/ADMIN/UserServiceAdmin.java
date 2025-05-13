package com.TaskManagement.SpringBoot.service.User.ADMIN;

import com.TaskManagement.SpringBoot.model.UserAdmin;
import com.TaskManagement.SpringBoot.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceAdmin {

    @Autowired
    private AdminUserRepository adminRepo;

    public UserAdmin registerAdmin(String fullName,
                                   String email,
                                   String passwordHash,
                                   String mobileNumber) {
        UserAdmin admin = new UserAdmin();
        admin.setFullName(fullName);
        admin.setEmail(email);
        admin.setPasswordHash(passwordHash);
        admin.setMobileNumber(mobileNumber);
        admin.setRole(Role.ADMIN);
        return adminRepo.save(admin);
    }

    public Optional<UserAdmin> findByEmail(String email) {
        return adminRepo.findByEmail(email);
    }

    public Optional<UserAdmin> findById(Long id) {
        return adminRepo.findById(id);
    }
}
