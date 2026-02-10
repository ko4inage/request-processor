package org.ko4inage.requestprocessor.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public Executor taskExecutor() {
    // пул потоков для параллельной отправки
    return Executors.newFixedThreadPool(10);
  }
}