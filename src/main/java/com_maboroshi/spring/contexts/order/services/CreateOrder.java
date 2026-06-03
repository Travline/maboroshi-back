package com_maboroshi.spring.contexts.order.services;

import com_maboroshi.spring.contexts.order.errors.CannotCreateOrder;
import com_maboroshi.spring.contexts.order.models.OrderItem;
import com_maboroshi.spring.contexts.order.persistence.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrder {
  private final OrderRepository repository;

  public List<OrderItem> execute(UUID userId) {
    return repository
        .createOrder(userId)
        .orElseThrow(() -> new CannotCreateOrder("Cannot create order. Make sure cart is not empty and try again."));
  }
}
