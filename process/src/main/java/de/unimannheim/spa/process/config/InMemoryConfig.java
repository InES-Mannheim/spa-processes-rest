/*******************************************************************************
 * Copyright 2016 University of Mannheim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package de.unimannheim.spa.process.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import de.unima.core.application.SPA;
import de.unima.core.application.local.LocalSPA;

@Configuration
@Profile("development")
@ComponentScan("de.unimannheim.spa.process")
@SpringBootApplication
public class InMemoryConfig {

  //CORS
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurerAdapter() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        /**
         * Global CORS configuration which
         * enables all Cross Origin Requests
         */
        registry.addMapping("/**"); 
      }
    };
  }

  @Bean(name="SPA")
  public SPA initSPAInSharedMemeoryBean(){
    return LocalSPA.withDataInSharedMemory();
  }

  public static void main(String[] args) {
    SpringApplication.run(InMemoryConfig.class, args);
  }
}
