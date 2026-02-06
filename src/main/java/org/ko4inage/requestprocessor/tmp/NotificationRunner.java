package org.ko4inage.requestprocessor.tmp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationRunner implements CommandLineRunner {

    private final AsyncNotificationSender sender;

    @Override
    public void run(String... args) throws Exception {
        sender.sendNotificationsFromFile("http/notifications_for_example_small.json");
    }
}
