package org.ko4inage.requestprocessor.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ko4inage.requestprocessor.enums.Topic;

@Data
@NoArgsConstructor
public class NotificationRequest {

  @NotNull(message = "Type should not be null")
  private Topic type;

  @NotBlank(message = "Message should not be blank")
  private String message;
}
