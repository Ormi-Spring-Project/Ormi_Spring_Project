package com.team8.Spring_Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.team8.Spring_Project.infrastructure.persistence")
public class SpringProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringProjectApplication.class, args);
	}
}
