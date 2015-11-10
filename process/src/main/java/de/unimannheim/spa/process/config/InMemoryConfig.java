package de.unimannheim.spa.process.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("development")
@ComponentScan("de.unimannheim.spa.process")
public class InMemoryConfig {

}
