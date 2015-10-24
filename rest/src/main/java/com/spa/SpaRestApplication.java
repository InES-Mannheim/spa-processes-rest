package com.spa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SPA REST application provides a REST interfaces to SPA core.
 */
@SpringBootApplication
public class SpaRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpaRestApplication.class, args);
    }
}
