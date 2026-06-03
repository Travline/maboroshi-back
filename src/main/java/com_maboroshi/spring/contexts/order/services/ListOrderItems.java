package com_maboroshi.spring.contexts.order.services;

import com_maboroshi.spring.contexts.order.errors.OrderNotFound;
import com_maboroshi.spring.contexts.order.models.OrderItem;
import com_maboroshi.spring.contexts.order.persistence.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListOrderItems {
  private final OrderRepository repository;

  public List<OrderItem> execute(UUID userId) {
    return repository
        .findAllOrderItemsByUserId(userId)
        .orElseThrow(() -> new OrderNotFound("Could not retrieve orders/sales records for user: " + userId));
  }
}
