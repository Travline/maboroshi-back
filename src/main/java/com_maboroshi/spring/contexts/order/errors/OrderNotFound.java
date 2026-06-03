package com_maboroshi.spring.contexts.order.errors;

public class OrderNotFound extends RuntimeException {
  public OrderNotFound(String message) {
    super(message);
  }
}
