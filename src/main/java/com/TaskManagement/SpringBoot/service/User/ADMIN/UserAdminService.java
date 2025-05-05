package com.TaskManagement.SpringBoot.service.User.ADMIN;

import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.model.UserEmployee;
import com.TaskManagement.SpringBoot.repository.UserEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserAdminService {

    @Autowired
    private UserEmployeeRepository employeeRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UserEmployee createAdminIfNotExist() {
        String adminEmail = "mo.2301@gmail.com";
        Optional<UserEmployee> existingAdmin = employeeRepository.findByEmail(adminEmail);

        if (existingAdmin.isPresent()) {
            return existingAdmin.get();
        }

        UserEmployee admin = new UserEmployee();
        admin.setFullName("Admin");
        admin.setEmail(adminEmail);
        admin.setPasswordHash(passwordEncoder.encode("Aa@102030"));
        admin.setMobileNumber("0503369271");
        admin.setDepartment("Administration");
        admin.setJobTitle("Administrator");
        admin.setRole(Role.ADMIN_EMPLOYEE);
        return employeeRepository.save(admin);
    }
}
