package org.ko4inage.requestprocessor.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotificationExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable() {
    Map<String, Object> response = new HashMap<>();
    response.put("error", "INVALID_REQUEST");
    response.put("message", "Не верный формат запроса");
    response.put("expectedFormat", Map.of(
        "type", "(Enum) SMS|EMAIL|PUSH|TG_MESSAGE",
        "message", "string"
    ));

    return ResponseEntity
        .badRequest()
        .contentType(MediaType.APPLICATION_JSON)
        .body(response);
  }
}

