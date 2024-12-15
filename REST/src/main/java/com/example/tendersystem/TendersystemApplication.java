package com.example.tendersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Tenders sytem", contact = @Contact(name = "Marchenko", email = "marchenko.artem@lll.kpi.ua"), version = "1.2"))

@SpringBootApplication
public class TendersystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TendersystemApplication.class, args);
	}

}
