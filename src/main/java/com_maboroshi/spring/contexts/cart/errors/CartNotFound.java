package com_maboroshi.spring.contexts.cart.errors;

public class CartNotFound extends RuntimeException {
  public CartNotFound(String message) {
    super(message);
  }
}
