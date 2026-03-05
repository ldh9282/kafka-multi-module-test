package com.kafka01.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.kafka01.products",
                "com.kafka01.common"
        }
)
public class ProductsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsApplication.class, args);
    }

}
