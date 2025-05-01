package com.TaskManagement.SpringBoot;

import com.TaskManagement.SpringBoot.service.User.ADMIN.UserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserAdminService adminService;

    @Override
    public void run(String... args) throws Exception {
        adminService.createAdminIfNotExist();
        System.out.println("Admin user has been initialized ....");
    }
}
