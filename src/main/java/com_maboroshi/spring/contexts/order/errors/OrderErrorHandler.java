package com_maboroshi.spring.contexts.order.errors;

import com_maboroshi.spring.shared.utils.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@RequiredArgsConstructor
public class OrderErrorHandler {
  private final Slf4jLogger logger;

  @ExceptionHandler(UserUnauthenticated.class)
  public ResponseEntity<HashMap<String, String>> handleUserUnauthenticated(UserUnauthenticated ex) {
    var resBody = new HashMap<String, String>();
    resBody.put("error", ex.getMessage());
    logger.warn(ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resBody);
  }

  @ExceptionHandler(CannotCreateOrder.class)
  public ResponseEntity<HashMap<String, String>> handleCannotCreateOrder(CannotCreateOrder ex) {
    var resBody = new HashMap<String, String>();
    resBody.put("error", ex.getMessage());
    logger.warn(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody);
  }

  @ExceptionHandler(OrderNotFound.class)
  public ResponseEntity<HashMap<String, String>> handleOrderNotFound(OrderNotFound ex) {
    var resBody = new HashMap<String, String>();
    resBody.put("error", ex.getMessage());
    logger.warn(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resBody);
  }
}
