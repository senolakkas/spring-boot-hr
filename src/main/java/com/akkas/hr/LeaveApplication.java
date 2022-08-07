package com.akkas.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class LeaveApplication {
  public static void main(String[] args) {
    SpringApplication.run(LeaveApplication.class, args);
  }
}
