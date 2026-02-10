package org.ko4inage.requestprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RequestProcessorApplication {

  public static void main(String[] args) {
    SpringApplication.run(RequestProcessorApplication.class, args);
  }

}
