package com.example.scoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Application entry point for the scoring microservice.
 */
@SpringBootApplication
@EnableAsync
public class ScoringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoringServiceApplication.class, args);
    }
}
