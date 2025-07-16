package com.proy.diseno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.proy.diseno")
public class DisenoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisenoApplication.class, args);
	}

}
