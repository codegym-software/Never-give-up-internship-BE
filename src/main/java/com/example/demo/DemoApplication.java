package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		// Load .env
        Dotenv dotenv = Dotenv.load();
        
        // Set tất cả biến vào System properties để Spring đọc được
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		
		SpringApplication.run(DemoApplication.class, args);
	}

}
