package org.ko4inage.requestprocessor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.requestprocessor.api.request.NotificationRequest;
import org.ko4inage.requestprocessor.model.Message;
import org.ko4inage.requestprocessor.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@Slf4j
public class NotificationController {

  private final NotificationService notificationService;

  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @PostMapping
  public ResponseEntity<Message> createNotification(@RequestBody @Valid NotificationRequest notificationRequest) throws JsonProcessingException {
    log.info("Получен запрос: {}", notificationRequest);
    Message message = notificationService.saveNotification(notificationRequest);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(message);
  }
}
