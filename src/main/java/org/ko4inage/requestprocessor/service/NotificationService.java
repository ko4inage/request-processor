package org.ko4inage.requestprocessor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.requestprocessor.api.request.NotificationRequest;
import org.ko4inage.requestprocessor.model.Message;
import org.ko4inage.requestprocessor.model.NotificationOutbox;
import org.ko4inage.requestprocessor.dto.MessageDto;
import org.ko4inage.requestprocessor.repo.MessageRepository;
import org.ko4inage.requestprocessor.repo.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final MessageRepository messageRepository;
  private final ObjectMapper objectMapper;

  @Transactional
  public Message saveNotification(NotificationRequest notificationRequest) throws JsonProcessingException {

    // Сохраняем бизнес-логику
    Message message = createMessage(notificationRequest);
    messageRepository.save(message);

    // Сохраняем в outbox
    NotificationOutbox note = createNotification(notificationRequest);
    notificationRepository.save(note);

    log.info("Подготовлено сообщение для отправки. Key: {}, Payload: {}, topic: {}",
        note.getKey(),
        note.getValue(),
        note.getTopic());

    return message;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void incrementAttempt(UUID id) {
    notificationRepository.incrementAttempt(id);
  }

  private NotificationOutbox createNotification(NotificationRequest notificationRequest) throws JsonProcessingException {

    MessageDto msg = new MessageDto(notificationRequest.getMessage());
    String json = objectMapper.writeValueAsString(msg);

    return NotificationOutbox.builder()
        .topic(notificationRequest.getType().getTopicName())
        .key(UUID.randomUUID().toString())
        .value(json)
        .sent(false)
        .build();
  }

  private Message createMessage(NotificationRequest notificationRequest) {
    return Message.builder()
        .type(notificationRequest.getType().toString())
        .message(notificationRequest.getMessage())
        .build();
  }

  NotificationOutbox findById(UUID id) {
    return notificationRepository.findById(id).orElseThrow();
  }
}
