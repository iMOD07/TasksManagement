package com.TaskManagement.SpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication // 30-04-2025, 04:08 PM
@EnableMethodSecurity
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}


