package com.owiseman.dataapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.owiseman.dataapi.entity")
public class DataapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataapiApplication.class, args);
	}

}
