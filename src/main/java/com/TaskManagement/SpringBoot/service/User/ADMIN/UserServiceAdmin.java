package com.TaskManagement.SpringBoot.service.User.ADMIN;

import com.TaskManagement.SpringBoot.model.UserAdmin;
import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.repository.Users.UserAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceAdmin {

    @Autowired
    private UserAdminRepository userAdminRepository;

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
        return userAdminRepository.save(admin);
    }

    public Optional<UserAdmin> findByEmail(String email) {
        return userAdminRepository.findByEmail(email);
    }

    public Optional<UserAdmin> findById(Long id) {
        return userAdminRepository.findById(id);
    }
}
