package com.andre.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class CuentaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuentaServiceApplication.class, args);
	}

}
