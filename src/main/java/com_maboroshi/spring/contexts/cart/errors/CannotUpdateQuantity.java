package com_maboroshi.spring.contexts.cart.errors;

public class CannotUpdateQuantity extends RuntimeException {
  public CannotUpdateQuantity(String message) {
    super(message);
  }
}
