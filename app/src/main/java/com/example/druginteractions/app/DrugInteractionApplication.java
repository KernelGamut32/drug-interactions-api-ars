package com.example.druginteractions.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.example.druginteractions")
@EnableAsync
public class DrugInteractionApplication {
  public static void main(String[] args) {
    SpringApplication.run(DrugInteractionApplication.class, args);
  }
}
