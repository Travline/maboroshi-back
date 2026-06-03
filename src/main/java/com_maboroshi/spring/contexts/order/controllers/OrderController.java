package com_maboroshi.spring.contexts.order.controllers;

import com_maboroshi.spring.contexts.order.errors.UserUnauthenticated;
import com_maboroshi.spring.contexts.order.models.OrderItem;
import com_maboroshi.spring.contexts.order.services.CreateOrder;
import com_maboroshi.spring.contexts.order.services.ListOrderItems;
import com_maboroshi.spring.shared.utils.SessionCookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {

  private final SessionCookie cookie;
  private final CreateOrder createOrder;
  private final ListOrderItems listOrderItems;

  @PostMapping
  public ResponseEntity<List<OrderItem>> newOrder(
      @CookieValue(name = "maboroshi-token", required = false) String token) {

    UUID userId = extractUserIdOrThrow(token);

    List<OrderItem> response = createOrder.execute(userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<List<OrderItem>> getOrders(
      @CookieValue(name = "maboroshi-token", required = false) String token) {

    UUID userId = extractUserIdOrThrow(token);

    List<OrderItem> response = listOrderItems.execute(userId);
    return ResponseEntity.ok(response);
  }

  private UUID extractUserIdOrThrow(String token) {
    var cookieRes = cookie.getUserIdFromSessionCookie(token);
    if (!cookieRes.isSucces) {
      throw new UserUnauthenticated("Session cookie expired or invalid");
    }
    return cookieRes.getValue();
  }
}
