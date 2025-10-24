package com.ra2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ra2Application{

    @Value("${spring.datasource.url:NOT_FOUND}")
    private String dbUrl;

    public static void main(String[] args) {
        SpringApplication.run(Ra2Application.class, args);
    }
}
