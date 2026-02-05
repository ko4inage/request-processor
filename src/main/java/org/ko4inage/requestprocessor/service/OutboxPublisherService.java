package org.ko4inage.requestprocessor.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.requestprocessor.config.OutboxProperties;
import org.ko4inage.requestprocessor.model.NotificationOutbox;
import org.ko4inage.requestprocessor.repo.NotificationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherService {

    private final NotificationRepository notificationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxProperties properties;

    @Scheduled(fixedDelayString = "#{@outboxProperties.delayMs}")
    @Transactional
    public void publishOutbox() {

        Pageable limit = PageRequest.of(0, properties.getBatchSize());

        List<NotificationOutbox> notifications =
                notificationRepository.findBatch(limit);

        if(notifications.isEmpty()){
            return;
        }

        for (NotificationOutbox notification : notifications) {
            try {
                kafkaTemplate.send(
                        notification.getTopic(),
                        notification.getKey(),
                        notification.getValue()
                );

                notification.setSent(true);
                //notificationRepository.save(notification);

                log.info("Уведомление отправлено: {}, попытка={}",
                        notification.getId(),
                        notification.getAttempt());
            } catch (Exception e) {
                notification.setAttempt(notification.getAttempt() + 1);

                log.warn("Неудачная попытка отправки сообщения: {}, attempt={}",
                        notification.getId(),
                        notification.getAttempt(),
                        e);
            }
        }
    }
}