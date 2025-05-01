package com.TaskManagement.SpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication // 01-05-2025, 04:46 PM
@EnableMethodSecurity
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}


/*

اول شي تطبيقي ال frontend بسويه ب Flutter
انا ك ادمن
احتاج اول ما افتح التطبيق تجيني طلبات العملاء اللي هي ال Ticket
واضغط عليها تفتح لي صفحة انشاء مهمة Task ل الموظف اللي عندي بجدول الموظفين

 */