package de.unimannheim.ines.spa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SPA application provides a REST interfaces to SPA processes.
 */
@SpringBootApplication
public class Spa {

    public static void main(String[] args) {
        SpringApplication.run(Spa.class, args);
    }
}
