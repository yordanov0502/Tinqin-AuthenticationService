package com.tinqinacademy.authenticationservice.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.tinqinacademy.authenticationservice.persistence.repository")
@EntityScan(basePackages = "com.tinqinacademy.authenticationservice.persistence.model.entity")
@ComponentScan(basePackages = "com.tinqinacademy.authenticationservice")
public class AuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

}
