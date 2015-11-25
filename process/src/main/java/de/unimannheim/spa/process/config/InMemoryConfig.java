package de.unimannheim.spa.process.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;

@Configuration
@Profile("development")
@ComponentScan("de.unimannheim.spa.process")
@SpringBootApplication
public class InMemoryConfig {
  
  //CORS
  @Bean
  FilterRegistrationBean corsFilter(@Value("${tagit.origin:http://localhost}") String origin) {
      return new FilterRegistrationBean(new Filter() {
          public void doFilter(ServletRequest req, ServletResponse res,
                  FilterChain chain) throws IOException, ServletException {
              HttpServletRequest request = (HttpServletRequest) req;
              HttpServletResponse response = (HttpServletResponse) res;
              String method = request.getMethod();
              // this origin value could just as easily have come from a database
              response.setHeader("Access-Control-Allow-Origin", origin);
              response.setHeader("Access-Control-Allow-Methods",
                      "POST,GET,OPTIONS,DELETE");
              response.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
              response.setHeader("Access-Control-Allow-Credentials", "true");
              response.setHeader(
                      "Access-Control-Allow-Headers",
                      "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
              if ("OPTIONS".equals(method)) {
                  response.setStatus(HttpStatus.OK.value());
              }
              else {
                  chain.doFilter(req, res);
              }
          }

          public void init(FilterConfig filterConfig) {
          }

          public void destroy() {
          }
      });
  }
  
  public static void main(String[] args) {
    SpringApplication.run(InMemoryConfig.class, args);
  }
}
