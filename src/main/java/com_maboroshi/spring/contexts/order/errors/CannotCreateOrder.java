package com_maboroshi.spring.contexts.order.errors;

public class CannotCreateOrder extends RuntimeException {
  public CannotCreateOrder(String message) {
    super(message);
  }
}
