package org.ko4inage.requestprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.requestprocessor.model.NotificationOutbox;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SinglePublisher {

    private final NotificationService notificationService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void publish(UUID id) throws Exception {

        NotificationOutbox notification = notificationService.findById(id);

        try {
            kafkaTemplate.send(
                    notification.getTopic(),
                    notification.getKey(),
                    notification.getValue()
            ).get(5, TimeUnit.SECONDS);

            notification.setSent(true);

            log.info("Сообщение отправлено: {}, попытка={}",
                    notification.getKey(),
                    notification.getAttempt());
        } catch (Exception e) {
            notificationService.incrementAttempt(notification.getId());
            throw e;
        }

    }
}
