package com.steven.willysbakeshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class WillysbakeshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(WillysbakeshopApplication.class, args);
	}
}
