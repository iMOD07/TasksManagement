package com.TaskManagement.SpringBoot.service.User.ADMIN;

import com.TaskManagement.SpringBoot.model.UserClient;
import com.TaskManagement.SpringBoot.repository.UserClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserAdminClient {

    @Autowired
    private UserClientRepository ClientRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UserClient createAdminIfNotExistClient() {
        String adminEmail = "slm@hotmail.com";
        Optional<UserClient> existingAdmin = ClientRepository.findByEmail(adminEmail);

        if (existingAdmin.isPresent()) {
            return existingAdmin.get();
        }

        UserClient admin = new UserClient();
        admin.setFullName("AdminClient");
        admin.setEmail(adminEmail);
        admin.setPasswordHash(passwordEncoder.encode("Aa@102030"));
        admin.setMobileNumber("0503369271");
        admin.setCompanyName("ADMIN");
        admin.setAddress("ADMIN");
        admin.setRole(com.TaskManagement.SpringBoot.model.Role.ADMIN);
        return ClientRepository.save(admin);
    }
}