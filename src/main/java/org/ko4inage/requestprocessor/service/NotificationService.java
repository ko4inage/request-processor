package org.ko4inage.requestprocessor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.ko4inage.requestprocessor.api.request.NotificationRequest;
import org.ko4inage.requestprocessor.model.Message;
import org.ko4inage.requestprocessor.model.NotificationOutbox;
import org.ko4inage.requestprocessor.repo.MessageRepository;
import org.ko4inage.requestprocessor.repo.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Message saveNotification(NotificationRequest notificationRequest) throws JsonProcessingException {

        //Сохраняем бизнес-логику
        Message message = createMessage(notificationRequest);
        messageRepository.save(message);

        //Сохраняем в outbox
        NotificationOutbox note = createNotification(notificationRequest);
        notificationRepository.save(note);

        log.info("Подготовлено сообщение для отправки. Key: {}, Payload: {}, topic: {}",
                note.getKey(),
                note.getValue(),
                note.getTopic());

        return message;
    }

    private NotificationOutbox createNotification(NotificationRequest notificationRequest) throws JsonProcessingException {

        String json = objectMapper.writeValueAsString(notificationRequest.getMessage());

        NotificationOutbox note = new NotificationOutbox();
        note.setTopic(notificationRequest.getType().getTopicName());
        note.setKey(UUID.randomUUID().toString());
        note.setValue(json);
        note.setSent(false);
        return note;
    }

    private Message createMessage(NotificationRequest notificationRequest) {
        Message message = new Message();
        message.setType(notificationRequest.getType().toString());
        message.setMessage(notificationRequest.getMessage());
        return message;
    }

}
