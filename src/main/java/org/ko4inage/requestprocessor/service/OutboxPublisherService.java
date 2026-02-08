package org.ko4inage.requestprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ko4inage.requestprocessor.config.OutboxProperties;
import org.ko4inage.requestprocessor.model.NotificationOutbox;
import org.ko4inage.requestprocessor.repo.NotificationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherService {

    private final NotificationRepository notificationRepository;
    private final OutboxProperties properties;
    private final SinglePublisher singlePublisher;

    @Scheduled(fixedDelayString = "#{@outboxProperties.delayMs}")
    public void publishOutbox() {

        Pageable limit = PageRequest.of(0, properties.getBatchSize());

        List<NotificationOutbox> notifications = notificationRepository.findBatch(limit);

        for (NotificationOutbox notification : notifications) {
            try {
                singlePublisher.publish(notification.getId());
            } catch (Exception e) {
                log.warn("Неудачная попытка отправки сообщения: {}, attempt={}",
                        notification.getKey(),
                        notification.getAttempt(),
                        e);
            }
        }

    }
}