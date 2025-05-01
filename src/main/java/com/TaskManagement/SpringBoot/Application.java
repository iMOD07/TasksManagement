package com.TaskManagement.SpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication // 01-05-2025, 10:33 AM
@EnableMethodSecurity
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
