package org.ko4inage.requestprocessor.tmp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.ko4inage.requestprocessor.api.request.NotificationRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AsyncNotificationSender {

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;
  private final Executor taskExecutor;

  public void sendNotificationsFromFile(String filePath) throws Exception {
    File file = new File(filePath);
    List<NotificationRequest> requests = mapper.readValue(
        file, new TypeReference<List<NotificationRequest>>() {
        }
    );

    String url = "http://localhost:8080/api/v1/notifications";

    // создаём CompletableFuture для каждой записи
    List<CompletableFuture<Void>> futures = requests.stream()
        .map(req -> CompletableFuture.runAsync(() -> send(req, url), taskExecutor))
        .toList();

    // ждём завершения всех задач
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
  }

  private void send(NotificationRequest req, String url) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<NotificationRequest> entity = new HttpEntity<>(req, headers);

      restTemplate.postForEntity(url, entity, String.class);

      System.out.println("Отправлено: " + req.getType() + " -> " + req.getMessage());

    } catch (Exception e) {
      System.err.println("Ошибка при отправке " + req.getType() + ": " + e.getMessage());
    }
  }
}
