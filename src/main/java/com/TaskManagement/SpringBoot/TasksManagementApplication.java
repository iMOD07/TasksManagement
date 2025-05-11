package com.TaskManagement.SpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // 👈 مهم
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@ComponentScan(basePackages = "com.TaskManagement.SpringBoot") // 👈 ضروري جداً
@EnableMethodSecurity(prePostEnabled = true)
public class TasksManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(TasksManagementApplication.class, args);
	}
}