package com.springboot.careassist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class CareassistBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareassistBackendApplication.class, args);
	}

}
