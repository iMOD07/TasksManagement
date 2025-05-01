package com.TaskManagement.SpringBoot;

import com.TaskManagement.SpringBoot.service.User.ADMIN.UserAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializerClient implements CommandLineRunner {

    @Autowired
    private UserAdminClient adminClient;

    @Override
    public void run(String... args) throws Exception {
        adminClient.createAdminIfNotExistClient();
        System.out.println("Admin Client user has been initialized ...");
    }
}
