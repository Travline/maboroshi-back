package com_maboroshi.spring.contexts.order.persistence;

import com_maboroshi.spring.contexts.order.models.OrderItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
  Optional<List<OrderItem>> createOrder(UUID userId);
  Optional<List<OrderItem>> findAllOrderItemsByUserId(UUID userId);
}
