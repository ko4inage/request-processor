package org.ko4inage.requestprocessor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.ko4inage.requestprocessor.api.request.NotificationRequest;
import org.ko4inage.requestprocessor.model.Message;
import org.ko4inage.requestprocessor.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

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
