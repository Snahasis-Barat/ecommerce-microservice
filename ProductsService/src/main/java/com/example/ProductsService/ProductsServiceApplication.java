package com.example.ProductsService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}

}
