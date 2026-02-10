package org.ko4inage.requestprocessor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.outbox")
@Data
public class OutboxProperties {
  private int batchSize;
  private long delayMs;
}